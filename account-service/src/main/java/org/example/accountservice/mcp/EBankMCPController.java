package org.example.accountservice.mcp;

import org.example.accountservice.domain.Account;
import org.example.accountservice.domain.AccountTransaction;
import org.example.accountservice.domain.AccountType;
import org.example.accountservice.exceptions.AccountNotFoundException;
import org.example.accountservice.exceptions.BalanceNotSufficientException;
import org.example.accountservice.exceptions.NegativeAmountException;
import org.example.accountservice.service.EBankService;
import org.springframework.ai.mcp.annotation.McpArg;
import org.springframework.ai.mcp.annotation.McpTool;
import org.springframework.stereotype.Component;


import javax.swing.*;
import java.util.List;

@Component
public class EBankMCPController {

    private EBankService eBankService;

    public EBankMCPController(EBankService eBankService){

        this.eBankService=eBankService;
    }


    @McpTool(description = "Get Account By Id")
    public Account getAccountById(@McpArg(description = "The account id") String accountId) throws AccountNotFoundException {
        return eBankService.getAccountById(accountId);
    }

    @McpTool(description = "Save a new Account")
    public Account saveNewAccount(@McpArg(description = "The initial balance") double initialBalance,@McpArg(description = "The account type") AccountType type,@McpArg(description = "The account currency") String currency,@McpArg(description = "The customer id") Long customerId) throws NegativeAmountException {
        return eBankService.saveNewAccount(initialBalance, type, currency, customerId);
    }

    @McpTool(description = "Credit Account")
    public AccountTransaction credit(@McpArg(description = "The account id") String accountId, @McpArg(description = "The amount") double amount) throws AccountNotFoundException, NegativeAmountException {
        return eBankService.credit(accountId, amount);
    }
    @McpTool(description = "Get All Accounts ")
    public List<Account> getAllAccounts() {
        return eBankService.getAllAccounts();
    }

    @McpTool(description = "Debit Account")
    public AccountTransaction debit(@McpArg(description = "The account id") String accountId,@McpArg(description = "The ammount") double amount) throws AccountNotFoundException, NegativeAmountException, BalanceNotSufficientException {
        return eBankService.debit(accountId, amount);
    }





}
