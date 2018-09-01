package com.example.service;

import java.util.List;

import com.example.model.Product;

public interface ProductService {
	public List<Product> findAll();

	public Product findByName(String name);

	public void saveProduct(Product product);
}
