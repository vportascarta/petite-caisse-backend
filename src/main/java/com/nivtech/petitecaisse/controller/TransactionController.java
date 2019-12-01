package com.nivtech.petitecaisse.controller;

import com.nivtech.petitecaisse.controller.payload.AddTransactionRequest;
import com.nivtech.petitecaisse.domain.TransactionService;
import com.nivtech.petitecaisse.domain.UserService;
import com.nivtech.petitecaisse.domain.entity.Transaction;
import com.nivtech.petitecaisse.exception.BadRequestException;
import com.nivtech.petitecaisse.security.CurrentUser;
import com.nivtech.petitecaisse.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/api/transaction")
public class TransactionController
{
    private final TransactionService transactionService;

    private final UserService userService;

    @Autowired
    public TransactionController(TransactionService transactionService, UserService userService)
    {
        this.transactionService = transactionService;
        this.userService = userService;
    }

    @PostMapping("/add-all")
    public ResponseEntity<?> addTransactions(@Valid @RequestBody List<AddTransactionRequest> addTransactionRequests,
            @CurrentUser UserPrincipal userPrincipal)
    {
        var isValid = addTransactionRequests.stream()
                .allMatch(addTransactionRequest -> userPrincipal.getId().equals(addTransactionRequest.getBuyerId()));

        if (!isValid)
        {
            throw new BadRequestException(
                    "L'authentification a échouée, veuillez contacter l'administrateur pour plus d'info");
        }

        var result = transactionService.addTransactions(addTransactionRequests);

        return ResponseEntity.ok(result);
    }

    @PostMapping("/add")
    public ResponseEntity<?> addTransaction(@Valid @RequestBody AddTransactionRequest addTransactionRequest,
            @CurrentUser UserPrincipal userPrincipal)
    {
        var isValid = userPrincipal.getId().equals(addTransactionRequest.getBuyerId()) || userService
                .isUserCodeValid(addTransactionRequest.getBuyerId(), addTransactionRequest.getCode());

        if (!isValid)
        {
            throw new BadRequestException(
                    "L'authentification a échouée, veuillez contacter l'administrateur pour plus d'info");
        }

        var result = transactionService
                .addTransaction(addTransactionRequest.getDebtorId(), addTransactionRequest.getBuyerId(),
                        addTransactionRequest.getProductId(), addTransactionRequest.getQuantity());

        return ResponseEntity.ok(result);
    }

    @GetMapping("/last")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public @ResponseBody
    Page<Transaction> getLastTransactions(@RequestParam Integer page, @RequestParam Integer size)
    {
        return transactionService.getLastTransactions(page != null ? page : 0, size != null ? size : 10);
    }

    @DeleteMapping("/delete")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> deleteTransaction(@RequestParam Long id)
    {
        transactionService.deleteTransaction(id);

        return ResponseEntity.ok(true);
    }
}
