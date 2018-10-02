package com.example.model;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.validator.constraints.NotEmpty;

@Entity
@Table(name = "button")
public class Button {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "button_id")
	private long id;
	@Column(name = "name")
	@NotEmpty(message = "*Por favor, informe o nome do botão")
	private String name;

	@Column(name = "outros")
	private Boolean outros;

	public Boolean getOutros() {
		return outros;
	}

	public void setOutros(Boolean outros) {
		this.outros = outros;
	}

	public BigDecimal getValue() {
		return value;
	}

	public void setValue(BigDecimal value) {
		this.value = value;
	}

	@Column(name = "value")
	private BigDecimal value;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}