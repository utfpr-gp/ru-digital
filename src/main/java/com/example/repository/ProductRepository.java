package com.example.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.model.Product;

@Repository("productRepository")
public interface ProductRepository extends JpaRepository<Product, Long> {
	Product findByName(String name);

	Page<Product> findAll(Pageable pageable);

}
