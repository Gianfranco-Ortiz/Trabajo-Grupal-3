package com.upc.ejercicio_3.service.impl;

import com.upc.ejercicio_3.model.Account;
import com.upc.ejercicio_3.repository.AccountRepository;
import com.upc.ejercicio_3.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;

import java.awt.print.Book;

public class AccountServiceImpl implements AccountService {
    @Autowired
    private AccountRepository accountRepository;

    @Override
    public Account createAccount(Account account) {
        return accountRepository.save(account);
    }
}
