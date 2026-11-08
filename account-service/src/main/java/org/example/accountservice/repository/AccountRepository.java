package org.example.accountservice.repository;

import org.example.accountservice.domain.Account;
import org.springframework.data.jpa.repository.JpaRepository;



public interface AccountRepository extends JpaRepository<Account,String>{


}

