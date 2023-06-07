package com.upc.ejercicio_3.repository;

import com.upc.ejercicio_3.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    //List<Transaction> findByNameCustomer(String nameCustomer);
    //List<Transaction> findByCreateDateRange(String startDate, String endDate);
}
