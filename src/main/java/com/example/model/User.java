package com.example.model;

import java.math.BigDecimal;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

@Entity
@Table(name = "user")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "user_id")
	private Long id;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "email")
	@Email(message = "*Por favor informe o email")
	@NotEmpty(message = "*Por favor informe um email")
	private String email;
	@Column(name = "password")
	@Length(min = 5, message = "*Sua senha precisa ter ao menos 5 caracters")
	@NotEmpty(message = "*Por favor informe a senha")
	private String password;
	@Column(name = "name")
	@NotEmpty(message = "*Por favor informe o nome")
	private String name;

	@Column(name = "active")
	private int active;
	@Column(name = "balance")
	private BigDecimal balance;

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	@Column(name = "image")
	private String image;
	@Column(name = "document")
	@NotEmpty(message = "*Por favor informe o seu documento")
	private String document;

	public BigDecimal getBalance() {
		return balance;
	}

	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}

	public String getDocument() {
		return document;
	}

	public void setDocument(String document) {
		this.document = document;
	}

	@ManyToMany(cascade = { CascadeType.ALL })

	private Set<Role> roles;

	public User() {
	}

	public User(String name, Set<Role> roles) {
		this.name = name;
		this.roles = roles;
	}

	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public int getActive() {
		return active;
	}

	public void setActive(int active) {
		this.active = active;
	}

	/*
	 * public Set<Role> getRoles() { return roles; }
	 * 
	 * public void setRoles(Set<Role> roles) { this.roles = roles; }
	 */

}
