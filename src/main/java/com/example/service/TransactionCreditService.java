package com.example.service;

import java.text.ParseException;
import java.util.List;

import com.example.model.TransactionCredit;

public interface TransactionCreditService {
	public void saveTransaction(TransactionCredit transactioncredit);

	public List<TransactionCredit> findAll();

	public List<String> listdiferentButtons();

	public List<TransactionCredit> listFilter(List<String> names, List<String> operador, String dataini, String datafim,
			String tipo, String pars) throws ParseException;
}
