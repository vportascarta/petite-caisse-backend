package com.nivtech.petitecaisse.controller;

import com.nivtech.petitecaisse.controller.payload.UpdateBalanceRequest;
import com.nivtech.petitecaisse.controller.payload.UpdateRegisterRequest;
import com.nivtech.petitecaisse.controller.payload.UpdateUserInfoRequest;
import com.nivtech.petitecaisse.controller.payload.UpdateUserValidationRequest;
import com.nivtech.petitecaisse.domain.UserService;
import com.nivtech.petitecaisse.domain.entity.Transaction;
import com.nivtech.petitecaisse.domain.entity.User;
import com.nivtech.petitecaisse.security.CurrentUser;
import com.nivtech.petitecaisse.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Comparator;
import java.util.stream.Collectors;

@Controller
@RequestMapping(path = "/api/user")
public class UserController
{

    private final UserService userService;

    @Autowired
    public UserController(UserService userService)
    {
        this.userService = userService;
    }

    @GetMapping("/me")
    public @ResponseBody
    User getCurrentUser(@CurrentUser UserPrincipal userPrincipal)
    {
        var user = userService.getUser(userPrincipal.getId());
        user.setDebtTransactions(
                user.getDebtTransactions().stream().sorted(Comparator.comparing(Transaction::getPurchaseAt).reversed())
                        .limit(5).collect(Collectors.toList()));
        user.setBuyerTransactions(
                user.getBuyerTransactions().stream().sorted(Comparator.comparing(Transaction::getPurchaseAt).reversed())
                        .limit(5).collect(Collectors.toList()));
        return user;
    }

    @GetMapping(path = "/all-registers")
    public @ResponseBody
    Iterable<User> getAllCashRegister()
    {

        return userService.getAllCashRegisters();
    }

    @GetMapping(path = "/all")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_EXTERNAL')")
    public @ResponseBody
    Iterable<User> getAllUsers()
    {
        return userService.getAllUsers();
    }

    @PostMapping("/change-info")
    public ResponseEntity<?> changeUserInfo(@Valid @RequestBody UpdateUserInfoRequest updateUserInfoRequest,
            @CurrentUser UserPrincipal userPrincipal)
    {
        var user = userService.updateUser(userPrincipal.getId(), updateUserInfoRequest.getName(),
                updateUserInfoRequest.getImageUrl(), updateUserInfoRequest.getCode());

        return ResponseEntity.ok(user);
    }

    @PostMapping("/update-validation")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> updateUserValidation(
            @Valid @RequestBody UpdateUserValidationRequest updateUserValidationRequest)
    {
        var result = userService.updateUserValidation(updateUserValidationRequest.getUserId(),
                updateUserValidationRequest.getIsValidated());

        return ResponseEntity.ok(result);
    }

    @PostMapping("/update-balance")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> updateBalanceWithCashRegister(
            @Valid @RequestBody UpdateBalanceRequest updateBalanceRequest)
    {
        var result = userService
                .updateBalanceWithCashRegister(updateBalanceRequest.getRegisterId(), updateBalanceRequest.getUserId(),
                        updateBalanceRequest.getAmountToBeTransferred());

        return ResponseEntity.ok(result);
    }

    @PostMapping("/update-register")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> updateCashRegister(@Valid @RequestBody UpdateRegisterRequest updateRegisterRequest)
    {
        var result = userService
                .updateCashRegister(updateRegisterRequest.getRegisterId(), updateRegisterRequest.getNewAmount());

        return ResponseEntity.ok(result);
    }
}
