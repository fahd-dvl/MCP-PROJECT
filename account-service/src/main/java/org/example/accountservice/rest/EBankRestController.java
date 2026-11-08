package org.example.accountservice.rest;

import org.apache.coyote.Response;
import org.example.accountservice.domain.Account;
import org.example.accountservice.domain.AccountTransaction;
import org.example.accountservice.domain.AccountType;
import org.example.accountservice.exceptions.AccountNotFoundException;
import org.example.accountservice.exceptions.BalanceNotSufficientException;
import org.example.accountservice.exceptions.NegativeAmountException;
import org.example.accountservice.service.EBankService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
@RestController
public class EBankRestController {

    private final EBankService eBankService;

    public  EBankRestController(EBankService eBankService){
        this.eBankService=eBankService;
    }

    @GetMapping("/accounts/{accountId}")
    public Account getAccountById(@PathVariable  String accountId) throws AccountNotFoundException {
        return eBankService.getAccountById(accountId);
    }

    @GetMapping("/accounts")
    public List<Account> getAllAccounts() {
        return eBankService.getAllAccounts();
    }

    @PostMapping("/accounts/save")
    public Account saveNewAccount(@RequestParam double initialBalance,@RequestParam AccountType type,@RequestParam String currency,@RequestParam Long customerId) throws NegativeAmountException {
        return eBankService.saveNewAccount(initialBalance, type, currency, customerId);
    }

    @PostMapping("/accounts/credit")
    public AccountTransaction credit(@RequestParam String accountId,@RequestParam double amount) throws AccountNotFoundException, NegativeAmountException {
        return eBankService.credit(accountId, amount);
    }



    @PostMapping("/accounts/debit")
    public AccountTransaction debit(@RequestParam String accountId,@RequestParam double amount) throws AccountNotFoundException, NegativeAmountException, BalanceNotSufficientException {
        return eBankService.debit(accountId, amount);
    }








}
