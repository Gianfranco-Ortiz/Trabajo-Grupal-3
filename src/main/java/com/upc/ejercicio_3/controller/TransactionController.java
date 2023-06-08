package com.upc.ejercicio_3.controller;


import com.upc.ejercicio_3.exception.ValidationException;
import com.upc.ejercicio_3.model.Account;
import com.upc.ejercicio_3.model.Transaction;
import com.upc.ejercicio_3.repository.AccountRepository;
import com.upc.ejercicio_3.repository.TransactionRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/bank/v1")
public class TransactionController {
    private TransactionRepository transactionRepository;
    private AccountRepository accountRepository;

    public TransactionController(TransactionRepository transactionRepository, AccountRepository accountRepository) {
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
    }

    //URL: http://localhost:8080/api/bank/v1/transactions/filterByNameCustomer
    //Method: GET
    @Transactional(readOnly = true)
    @GetMapping("/transactions/filterByNameCustomer")
    public ResponseEntity<List<Transaction>> getAllTransactionsByNameCustomer(@RequestParam (name = "nameCustomer") String nameCustomer){
        Long accountId = accountRepository.findByNameCustomer(nameCustomer).getId();
        return new ResponseEntity<List<Transaction>>(transactionRepository.findByIdCustomer(accountId), HttpStatus.OK);
    }

    //URL: http://localhost:8080/api/bank/v1/transactions/filterByCreateDateRange
    //Method: GET
    @Transactional(readOnly = true)
    @GetMapping("/transactions/filterByCreateDateRange")
    public ResponseEntity<List<Transaction>> getAllTransactionsByCreateDateRange(@RequestParam (name = "startDate") LocalDate startDate,
                                                                                 @RequestParam (name = "endDate") LocalDate endDate){
        return new ResponseEntity<List<Transaction>>(transactionRepository.findTransactionByCreateDateRange(startDate, endDate), HttpStatus.OK);
    }

    //URL: http://localhost:8080/api/bank/v1/accounts/{id}/transactions
    //Method: POST
    @Transactional
    @PostMapping("/accounts/{id}/transactions")
    public ResponseEntity<Transaction> createTransaction(@PathVariable(value = "id") Long accountId, @RequestBody Transaction transaction) {
        Account account = accountRepository.findById(accountId).orElseThrow(() -> new RuntimeException("No se encuentra la cuenta con id: " + accountId));
        validateTransaction(transaction);
        transaction.setCreateDate(LocalDate.now());
        if(Objects.equals(transaction.getType(), "Deposito")){
            transaction.setBalance(transaction.getBalance() + transaction.getAmount());
        }else{
            transaction.setBalance(transaction.getBalance() - transaction.getAmount());
        }
        return new ResponseEntity<Transaction>(transactionRepository.save(transaction), HttpStatus.CREATED);
    }

    private void validateTransaction(Transaction transaction){
        if(transaction.getAccount().getId() == null){
            throw new ValidationException("El número de cuenta debe ser obligatorio");
        }
        if(transaction.getAccount().getId() <= 0){
            throw new ValidationException("Número de cuenta inválido");
        }
        if(transaction.getType() == null || transaction.getType().isEmpty()) {
            throw new ValidationException("El tipo de transacción bancaria debe ser obligatorio");
        }
        if(!transaction.getType().equals("Deposito")){
                if(!transaction.getType().equals("Retiro")){
                    throw new ValidationException("El tipo de transacción bancaria debe ser Deposito o Retiro");
                }
        }

        if(Objects.equals(transaction.getType(), "Retiro") && transaction.getBalance() < transaction.getAmount()){
            throw new ValidationException("En una transacción bancaria tipo retiro el monto a retirar no puede ser mayor al saldo");
        }

        if(transaction.getAmount() == null){
            throw new ValidationException("El monto de la transacción bancaria debe ser obligatorio");
        }
        if(transaction.getAmount() <= 0.0){
            throw new ValidationException("El monto en una transacción bancaria debe ser mayor de S/0.0");
        }
    }
}
