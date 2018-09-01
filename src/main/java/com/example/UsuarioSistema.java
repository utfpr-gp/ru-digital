package com.example;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

public class UsuarioSistema extends User {

	private static final long serialVersionUID = 1L;

	private String name;

	public UsuarioSistema(String name, String email, String password,
			Collection<? extends GrantedAuthority> authorities) {
		super(email, password, authorities);

		this.name = name;
	}

	public String getName() {
		return name;
	}
}