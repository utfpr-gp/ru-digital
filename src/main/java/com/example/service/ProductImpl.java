package com.example.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.model.Product;
import com.example.repository.ProductRepository;

@Service("productService")
public class ProductImpl implements ProductService {

	@Autowired
	private ProductRepository productRepository;

	@Override
	public void saveProduct(Product product) {
		productRepository.save(product);
	}

	@Override
	public List<Product> findAll() {
		return productRepository.findAll();
	}

	@Override
	public Product findByName(String name) {
		return productRepository.findByName(name);
	}

}
