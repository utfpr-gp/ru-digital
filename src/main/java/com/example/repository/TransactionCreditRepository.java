package com.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.model.TransactionCredit;

@Repository("transactioncreditRepository")
public interface TransactionCreditRepository extends JpaRepository<TransactionCredit, Long> {

}
