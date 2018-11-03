package com.example.repository;

import java.math.BigDecimal;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.model.Button;

@Repository("buttonRepository")
public interface ButtonRepository extends JpaRepository<Button, Long> {

	@Query(value = "SELECT * FROM button WHERE outros is null and deleted is null ORDER BY ?#{#pageable}", nativeQuery = true)
	Page<Button> activeButtons(Pageable pageable);

	@Transactional
	@Modifying
	@Query(value = "UPDATE button set name = ?1 , value = ?2 where button_id = ?3", nativeQuery = true)
	public void updateButton(String name, BigDecimal val, long id);

	@Query(value = "SELECT * FROM button WHERE  company_id = ?1 AND deleted IS NULL and outros IS NULL  ORDER BY ?#{#pageable}", nativeQuery = true)

	Page<Button> findAllButton(long company, Pageable pageable);

}
