package com.nivtech.petitecaisse.controller;

import com.nivtech.petitecaisse.controller.payload.DashboardResponse;
import com.nivtech.petitecaisse.domain.ProductService;
import com.nivtech.petitecaisse.domain.TransactionService;
import com.nivtech.petitecaisse.domain.UserService;
import com.nivtech.petitecaisse.domain.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping(path = "/api/dashboard")
public class DashboardController
{

    private final UserService userService;
    private final ProductService productService;
    private final TransactionService transactionService;

    @Autowired
    public DashboardController(UserService userService, ProductService productService,
            TransactionService transactionService)
    {
        this.userService = userService;
        this.productService = productService;
        this.transactionService = transactionService;
    }

    @GetMapping("/info")
    public @ResponseBody
    DashboardResponse getDashboardInfo()
    {
        var res = new DashboardResponse();

        res.setAmountInRegisters(userService.getAllCashRegisters().stream()
                .map(User::getBalance)
                .filter(Objects::nonNull)
                .map(Balance::getAmount)
                .reduce(Double::sum)
                .orElse(0.0)
        );

        res.setAmountInDebts(userService.getAllUsers().stream()
                .map(User::getBalance)
                .filter(Objects::nonNull)
                .map(Balance::getAmount)
                .reduce(Double::sum)
                .orElse(0.0)
        );

        var allProducts = productService.getAllProducts();

        res.setAmountInProducts(allProducts.stream()
                .map(product -> product.getStock() != null ? product.getPrice() * product.getStock()
                        .getQuantity() : 0.0)
                .reduce(Double::sum)
                .orElse(0.0)
        );

        res.setProductsInStock(allProducts.stream()
                .map(Product::getStock)
                .filter(Objects::nonNull)
                .map(Stock::getQuantity)
                .reduce(Integer::sum)
                .orElse(0)
        );

        var nbTransactionsByDate = transactionService
                .getLastTransactions(LocalDateTime.now().minusDays(7), LocalDateTime.now()).stream()
                .collect(Collectors.groupingBy(
                        transaction -> transaction.getPurchaseAt().toInstant().atZone(ZoneId.systemDefault())
                                .truncatedTo(ChronoUnit.DAYS),
                        Collectors.counting()
                ));

        var nbTransactionsByDateList = nbTransactionsByDate.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .map(entry -> Pair.of(entry.getKey().format(DateTimeFormatter.ISO_LOCAL_DATE), entry.getValue()))
                .collect(Collectors.toList());

        res.setPastSales(nbTransactionsByDateList);

        return res;
    }
}
