package com.example.model;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.validator.constraints.NotEmpty;

@Entity
@Table(name = "product")
public class Product {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "product_id")
	private int id;
	@Column(name = "name")
	@NotEmpty(message = "*Por favor, informe o nome do produto")
	private String name;
	@Column(name = "description")
	private String description;

	@ManyToMany(cascade = { CascadeType.ALL })
	private Set<TransactionDebit> transactiondebits;

	public Set<TransactionDebit> getTransactiondebits() {
		return transactiondebits;
	}

	public void setTransactiondebits(Set<TransactionDebit> transactiondebits) {
		this.transactiondebits = transactiondebits;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public BigDecimal getValue() {
		return value;
	}

	public void setValue(BigDecimal value) {
		this.value = value;
	}

	@Column(name = "value")
	private BigDecimal value;

	public int getId() {
		return id;
	}

	public List<Item> getItems() {
		return items;
	}

	public void setItems(List<Item> items) {
		this.items = items;
	}

	@Transient
	public List<Item> items;

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}