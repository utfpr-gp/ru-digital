package com.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.model.TransactionDebit;

@Repository("transactiondebitRepository")
public interface TransactionDebitRepository extends JpaRepository<TransactionDebit, Long> {

}
