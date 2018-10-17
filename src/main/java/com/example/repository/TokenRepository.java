package com.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.model.PasswordResetToken;

@Repository
public interface TokenRepository extends JpaRepository<PasswordResetToken, Long> {

	PasswordResetToken findByToken(String token);

	@Query(value = "SELECT * FROM passwordresettoken WHERE user_id = ?1 LIMIT 1", nativeQuery = true)
	PasswordResetToken hasToken(long id);
}
