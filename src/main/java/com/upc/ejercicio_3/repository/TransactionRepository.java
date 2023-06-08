package com.upc.ejercicio_3.repository;

import com.upc.ejercicio_3.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    @Query("SELECT t FROM Transaction t WHERE t.account.id = :id")
    List<Transaction> findByIdCustomer(@Param("id") Long id);
    @Query("SELECT t FROM Transaction t WHERE t.createDate BETWEEN :startDate AND :endDate")
    List<Transaction> findTransactionByCreateDateRange(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
}
