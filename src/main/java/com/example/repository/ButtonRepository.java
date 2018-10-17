package com.example.repository;

import java.math.BigDecimal;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.model.Button;

@Repository("buttonRepository")
public interface ButtonRepository extends JpaRepository<Button, Long> {
	@Transactional
	@Modifying
	@Query(value = "UPDATE button set name = ?1 , value = ?2 where button_id = ?3", nativeQuery = true)
	public void updateButton(String name, BigDecimal val, long id);

}
