package com.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.model.TransactionCredit;
import com.example.repository.TransactionCreditRepository;

@Service("transactioncreditService")
public class TransactionCreditImpl implements TransactionCreditService {

	@Autowired
	private TransactionCreditRepository transactioncreditRepository;

	@Override
	public void saveTransaction(TransactionCredit transactioncredit) {
		System.out.println("ME CHAMOU SIMMMMMMMMMMMMMMMMMmm");
		System.out.println(transactioncredit.getId());
		System.out.println("ME CHAMOU SIMMMMMMMMMMMMMMMMMmm");
		System.out.println(transactioncredit.getUser().getEmail());
		System.out.println("ME CHAMOU SIMMMMMMMMMMMMMMMMMmm");
		System.out.println(transactioncredit.getValue());
		System.out.println("ME CHAMOU SIMMMMMMMMMMMMMMMMMmm");
		transactioncreditRepository.save(transactioncredit);
	}

}
