package org.example.accountservice.service;

// Use Spring's Transactional for better control
import org.springframework.transaction.annotation.Transactional;
import org.example.accountservice.domain.*;
import org.example.accountservice.exceptions.AccountNotFoundException; // Use YOUR exception
import org.example.accountservice.exceptions.BalanceNotSufficientException;
import org.example.accountservice.exceptions.NegativeAmountException;
import org.example.accountservice.repository.AccountRepository;
import org.example.accountservice.repository.TransactionRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class EBankService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    public EBankService(AccountRepository accountRepository, TransactionRepository transactionRepository){
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
    }

    public Account getAccountById(String accountId) throws AccountNotFoundException {
        return accountRepository.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException("Account not found with id: " + accountId));
    }

    @Transactional
    public Account saveNewAccount(double initialBalance, AccountType type, String currency, Long customerId) throws NegativeAmountException {
        if(initialBalance < 0) throw new NegativeAmountException("The amount should be positive");

        Account account = Account.builder()
                .id(UUID.randomUUID().toString())
                .currency(currency)
                .type(type)
                .status(AccountStatus.CREATED) // Maybe change to ACTIVE after first credit?
                .createdAt(LocalDateTime.now())
                .customerId(customerId)
                .balance(initialBalance)
                .build();

        Account savedAccount = accountRepository.save(account);

        AccountTransaction transaction = AccountTransaction.builder()
                .amount(initialBalance)
                .type(TransactionType.CREDIT)
                .dateTime(LocalDateTime.now())
                .account(savedAccount)
                .build();

        transactionRepository.save(transaction);
        return savedAccount;
    }

    @Transactional
    public AccountTransaction credit(String accountId, double amount) throws AccountNotFoundException, NegativeAmountException {
        if (amount < 0) throw new NegativeAmountException("Amount should be positive");

        // In a real app, use a Pessimistic Lock here: getAccountByIdForUpdate(accountId)
        Account account = getAccountById(accountId);

        AccountTransaction transaction = AccountTransaction.builder()
                .amount(amount)
                .dateTime(LocalDateTime.now())
                .type(TransactionType.CREDIT)
                .account(account)
                .build();

        account.setBalance(account.getBalance() + amount);

        // Update status if needed: account.setStatus(AccountStatus.ACTIVE);

        accountRepository.save(account);
        return transactionRepository.save(transaction);
    }

    public List<Account> getAllAccounts(){
        return accountRepository.findAll();
    }

    @Transactional
    public AccountTransaction debit(String accountId, double amount) throws AccountNotFoundException, NegativeAmountException, BalanceNotSufficientException {
        if (amount < 0) throw new NegativeAmountException("Amount should be positive");

        Account account = getAccountById(accountId);

        if (account.getBalance() < amount) {
            throw new BalanceNotSufficientException("Balance is not sufficient. Current: " + account.getBalance());
        }

        AccountTransaction transaction = AccountTransaction.builder()
                .amount(amount)
                .dateTime(LocalDateTime.now())
                .type(TransactionType.DEBIT)
                .account(account)
                .build();

        account.setBalance(account.getBalance() - amount);

        // Save the account first, then the transaction
        accountRepository.save(account);
        return transactionRepository.save(transaction);
    }
}