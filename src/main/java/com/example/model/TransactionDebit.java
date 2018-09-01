package com.example.model;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "transactiondebit")
public class TransactionDebit {

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public BigDecimal getValue() {
		return value;
	}

	public void setValue(BigDecimal value) {
		this.value = value;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "transactioncredit_id")
	private int id;
	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@Column(name = "value")
	private BigDecimal value;

	@Column(name = "operator")
	private String operator;

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	@ManyToMany(mappedBy = "transactiondebits", cascade = { CascadeType.ALL })
	private List<Product> produtos;

	public List<Product> getProdutos() {
		return produtos;
	}

	public void setProdutos(List<Product> produtos) {
		this.produtos = produtos;
	}

}
