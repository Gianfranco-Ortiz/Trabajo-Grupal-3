package com.upc.ejercicio_3.repository;

import com.upc.ejercicio_3.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long> {
    Boolean existsByNameCustomerAndNumberAccount(String nameCustomer, String numberAccount);
}
