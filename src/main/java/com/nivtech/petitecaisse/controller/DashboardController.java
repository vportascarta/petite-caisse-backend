package com.nivtech.petitecaisse.controller;

import com.nivtech.petitecaisse.controller.payload.DashboardResponse;
import com.nivtech.petitecaisse.domain.ProductService;
import com.nivtech.petitecaisse.domain.UserService;
import com.nivtech.petitecaisse.domain.entity.Balance;
import com.nivtech.petitecaisse.domain.entity.Product;
import com.nivtech.petitecaisse.domain.entity.Stock;
import com.nivtech.petitecaisse.domain.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Objects;

@Controller
@RequestMapping(path = "/api/dashboard")
public class DashboardController
{

    private final UserService userService;
    private final ProductService productService;

    @Autowired
    public DashboardController(UserService userService, ProductService productService)
    {
        this.userService = userService;
        this.productService = productService;
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

        return res;
    }
}
