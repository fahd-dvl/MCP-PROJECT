package org.example.accountservice;

import org.example.accountservice.domain.AccountType;
import org.example.accountservice.exceptions.BalanceNotSufficientException;
import org.example.accountservice.exceptions.NegativeAmountException;
import org.example.accountservice.service.EBankService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class AccountServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(AccountServiceApplication.class, args);
    }

    @Bean
    CommandLineRunner commandLineRunner(EBankService eBankService){
        return args -> {
            try {
                eBankService.saveNewAccount(65000, AccountType.CURRENT_ACCOUNT, "MAD", Long.valueOf(1));
            } catch (NegativeAmountException e) {
                throw new RuntimeException(e);
            }
            try {
                eBankService.saveNewAccount(12000, AccountType.SAVING_ACCOUNT, "MAD", Long.valueOf(1));
            } catch (NegativeAmountException e) {
                throw new RuntimeException(e);
            }
            try {
                eBankService.saveNewAccount(3000, AccountType.CURRENT_ACCOUNT, "MAD", Long.valueOf(2));
            } catch (NegativeAmountException e) {
                throw new RuntimeException(e);
            }


            eBankService.getAllAccounts().forEach(account -> {
                for(int i=0;i<5;i++){
                    try {
                        eBankService.credit(account.getId(),500+Math.random()*23000);
                    } catch (NegativeAmountException e) {
                        throw new RuntimeException(e);
                    }
                    try {
                        eBankService.debit(account.getId(),500+Math.random()*1000);
                    } catch (NegativeAmountException e) {
                        throw new RuntimeException(e);
                    } catch (BalanceNotSufficientException e) {
                        throw new RuntimeException(e);
                    }

                }
            });


        };
    }

}

