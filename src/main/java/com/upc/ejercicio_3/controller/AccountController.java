package com.upc.ejercicio_3.controller;


import com.upc.ejercicio_3.exception.ValidationException;
import com.upc.ejercicio_3.model.Account;
import com.upc.ejercicio_3.repository.AccountRepository;
import com.upc.ejercicio_3.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bank/v1")
public class AccountController {

    //@Autowired
    //private AccountService accountService;

    private final AccountRepository accountRepository;

    public AccountController(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    //URL: http://localhost:8080/api/bank/v1/accounts
    //Method: GET
    @Transactional(readOnly = true)
    @GetMapping("/accounts")
    public ResponseEntity<List<Account>> getAllAccounts() {
        return new ResponseEntity<List<Account>>(accountRepository.findAll(), HttpStatus.OK);
    }

    //URL: http://localhost:8080/api/bank/v1/accounts
    //Method: POST
    @Transactional
    @PostMapping("/accounts")
    public ResponseEntity<Account> createAccount(@RequestBody Account account) {
        validateAccount(account);
        existsAccountByNameCustomerAndNumberAccount(account.getNameCustomer(),
                account.getNumberAccount());
        return new ResponseEntity<Account>(accountRepository.save(account), HttpStatus.CREATED);
    }

    private void validateAccount(Account account){
        if(account.getNumberAccount() == null || account.getNumberAccount().isEmpty()){
            throw new ValidationException("El número de cuenta debe ser obligatorio");
        }
        if(account.getNumberAccount().length() != 13){
            throw new ValidationException("El número de cuenta debe tener una longitud de 13 caracteres");
        }
        if(account.getNameCustomer() == null || account.getNameCustomer().isEmpty()) {
            throw new ValidationException("El nombre del cliente debe ser obligatorio");
        }
        if(account.getNameCustomer().length() > 30){
            throw new ValidationException("El nombre del cliente no debe exceder los 30 caracteres");
        }
    }

    private void existsAccountByNameCustomerAndNumberAccount(String nameCustomer, String numberAccount){
        if(accountRepository.existsByNameCustomerAndNumberAccount(nameCustomer, numberAccount)){
            throw new ValidationException("No se puede registrar la cuenta porque ya existe uno con estos datos");
        }
    }
}
