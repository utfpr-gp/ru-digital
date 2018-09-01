package com.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.model.TransactionDebit;
import com.example.repository.TransactionDebitRepository;

@Service("transactiondebitService")
public class TransactionDebitImpl implements TransactionDebitService {

	@Autowired
	private TransactionDebitRepository transactiondebitRepository;

	@Override
	public void saveTransaction(TransactionDebit transactiondebit) {
		transactiondebitRepository.save(transactiondebit);

	}

}
