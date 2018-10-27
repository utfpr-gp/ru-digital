package com.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.model.Company;

@Repository("companyRepository")
public interface CompanyRepository extends JpaRepository<Company, Long> {

}
