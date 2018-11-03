package com.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.model.Company;
import com.example.repository.CompanyRepository;

@Service("companyService")
public class CompanyServiceImpl implements CompanyService {

	@Autowired
	private CompanyRepository companyRepository;

	@Override
	public void saveCompany(Company c) {
		companyRepository.save(c);

	}

	public void updateCompany(Company company) {
		Company c = companyRepository.getOne(company.getId());
		c.setBalance(company.getBalance());
		c.setImage(company.getImage());
		c.setName(company.getName());
		companyRepository.save(c);
	}

}
