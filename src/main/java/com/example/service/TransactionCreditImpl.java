package com.example.service;

import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

	public List<TransactionCredit> findAll() {
		return transactioncreditRepository.findAll();
	}

	public Page<TransactionCredit> findAllUser(long user_id, Pageable pageable) {
		return transactioncreditRepository.findAllUser(user_id, pageable);
	}

	public Page<TransactionCredit> findByValuePostive(long company_id, long user_id, Pageable pageable) {
		return transactioncreditRepository.findByValuePostive(company_id, user_id, pageable);
	}

	public Page<TransactionCredit> findByValueNegative(long company_id, long user_id, Pageable pageable) {
		return transactioncreditRepository.findByValueNegative(company_id, user_id, pageable);
	}

	public Page<TransactionCredit> findByDate(long ini, long fim, long company_id, long user_id, Pageable pageable) {
		return transactioncreditRepository.findByDate(ini, fim, company_id, user_id, pageable);
	}

	public Page<TransactionCredit> findByTc(long ini, long fim, long company_id, long user_id, Pageable pageable) {
		return transactioncreditRepository.findByTc(ini, fim, company_id, user_id, pageable);
	}

	public Page<TransactionCredit> findByTd(long ini, long fim, long company_id, long user_id, Pageable pageable) {
		return transactioncreditRepository.findByTd(ini, fim, company_id, user_id, pageable);
	}

	@PersistenceContext
	private EntityManager manager;

	public List<String> listdiferentButtons() {

		System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>.");
		List<String> z = manager.createQuery("select distinct e.button.name from TransactionCredit e ").getResultList();
		z.add("Credito");
		return z;
	}

	public String dataToNumeral(String data) {
		String dia = data.substring(0, 2);
		String[] args = new String[] { "Janeiro,", "Fevereiro,", "Março,", "Abril,", "Maio,", "Junho,", "Julho,",
				"Agosto,", "Setembro,", "Outubro,", "Novembro,", "Dezembro," };
		System.out.println(dia);
		String aux = data.substring(3, data.length());
		String mes = aux.split("\\ ")[0];
		String ano = aux.split("\\ ")[1];
		System.out.println("VAI DAR CERTO MEU MES ???" + mes.substring(0, mes.length() - 1));
		for (int i = 0; i < 12; i++) {
			int ax = i + 1;
			if (args[i].equals(mes)) {
				if (i != 10 && i != 11 && i != 12) {
					mes = "0" + ax;
				} else
					mes = ax + "";
			}

		}

		System.out.println(mes);
		System.out.println(ano);
		return (ano + "/" + mes + "/" + dia);
	}

	public long dataToMili(String data) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

		Date date = sdf.parse(data);
		long millis = date.getTime();
		return millis;
	}

	public BigInteger totalTransactions() {
		BigInteger rowCnt = (BigInteger) manager.createNativeQuery("SELECT count(*) FROM transactioncredit")
				.getSingleResult();

		return rowCnt;
	}

	public List<TransactionCredit> listFilter(List<String> names, List<String> operador, String dataini, String datafim,
			String tipo, String pars) throws ParseException {
		boolean x = false;
		char[] characters = pars.toCharArray();
		String n = characters[0] + "";
		String o = characters[1] + "";
		String di = characters[2] + "";
		String df = characters[3] + "";
		String t = characters[4] + "";

		if (names == null && operador == null) {
			System.out.println("TA TUDO ANULADO");
		}
		List<TransactionCredit> z = null;
		dataini = dataToNumeral(dataini);
		dataini = dataini + " 00:00:01";
		datafim = dataToNumeral(datafim);
		datafim = datafim + " 23:59:59";
		long dinicial = dataToMili(dataini);
		long dfinal = dataToMili(datafim);
		if (n.equals("0") && o.equals("0") && di.equals("0") && df.equals("0") && t.equals("0")) {
			z = manager.createQuery("FROM TransactionCredit t WHERE t.button.name IN (:names) ")
					.setParameter("names", names).getResultList();
		}
		if (n.equals("1") && o.equals("1") && di.equals("1") && df.equals("1") && t.equals("1")) {
			if (tipo.equals("Debito"))
				z = manager
						.createQuery("FROM TransactionCredit t WHERE t.button.name IN (:names)  AND   "
								+ "t.operator IN (:operators) AND " + "t.milis between :inicial and :final" + " AND "
								+ " t.value < 0")
						.setParameter("names", names).setParameter("operators", operador)
						.setParameter("inicial", dinicial).setParameter("final", dfinal).getResultList();
			if (tipo.equals("Credito"))
				z = manager
						.createQuery("FROM TransactionCredit t WHERE (  t.button is NULL )  AND   "
								+ "t.operator IN (:operators) AND " + "t.milis between :inicial and :final")
						.setParameter("operators", operador).setParameter("inicial", dinicial)
						.setParameter("final", dfinal).getResultList();
		} else if (n.equals("1") && o.equals("1") && di.equals("1") && df.equals("1") && t.equals("0")) {
			z = manager
					.createQuery(
							"FROM TransactionCredit t WHERE (t.button.name IN (:names) OR  t.button is NULL  )  AND   "
									+ "t.operator IN (:operators) AND " + "t.milis between :inicial and :final")
					.setParameter("names", names).setParameter("operators", operador).setParameter("inicial", dinicial)
					.setParameter("final", dfinal).getResultList();

		}
		return z;
	}

}
