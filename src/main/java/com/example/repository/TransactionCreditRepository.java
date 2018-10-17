package com.example.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.model.TransactionCredit;

@Repository("transactioncreditRepository")
public interface TransactionCreditRepository extends JpaRepository<TransactionCredit, Long> {
	List<TransactionCredit> findAll();

	@Query(value = "SELECT * FROM transactioncredit WHERE value > 0  ORDER BY ?#{#pageable}", nativeQuery = true)

	Page<TransactionCredit> findByValuePostive(Pageable pageable);

	@Query(value = "SELECT * FROM transactioncredit WHERE value <  0  ORDER BY ?#{#pageable}", nativeQuery = true)
	Page<TransactionCredit> findByValueNegative(Pageable pageable);

	@Query(value = "SELECT * FROM transactioncredit WHERE milis >  ?1 and milis < ?2  ORDER BY ?#{#pageable}", nativeQuery = true)
	Page<TransactionCredit> findByDate(long ini, long fim, Pageable pageable);

	@Query(value = "SELECT * FROM transactioncredit WHERE (milis >  ?1 and milis < ?2) and value > 0  ORDER BY ?#{#pageable}", nativeQuery = true)
	Page<TransactionCredit> findByTc(long ini, long fim, Pageable pageable);

	@Query(value = "SELECT * FROM transactioncredit WHERE (milis >  ?1 and milis < ?2) and value < 0  ORDER BY ?#{#pageable}", nativeQuery = true)
	Page<TransactionCredit> findByTd(long ini, long fim, Pageable pageable);
}
