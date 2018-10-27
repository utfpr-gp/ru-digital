package com.example.repository;

import java.math.BigDecimal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.model.UserBalance;

@Repository("userbalanceRepository")
public interface UserBalanceRepository extends JpaRepository<UserBalance, Long> {

	@Query(value = "SELECT * FROM userbalance WHERE user_id = ?1 and company_id = ?2 LIMIT 1", nativeQuery = true)
	UserBalance hasBalance(long user_id, long company_id);

	@Query(value = "SELECT SUM(balance) AS 'total' FROM userbalance WHERE user_id = ?1", nativeQuery = true)
	BigDecimal sumBalances(long user_id);
}
