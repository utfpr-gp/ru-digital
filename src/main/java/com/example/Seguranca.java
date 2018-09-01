package com.example;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.configurers.provisioning.UserDetailsManagerConfigurer.UserDetailsBuilder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.example.model.Role;
import com.example.model.User;
import com.example.repository.RoleRepository;
import com.example.repository.UserRepository;
import com.example.service.UserService;

@Component
public class Seguranca implements UserDetailsService {

	@Autowired
	private UserRepository userrep;

	@Autowired
	private RoleRepository rolerep;

	@Autowired
	private UserService userService;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		System.out.println("--------____ME CHAMOU");
		User usuario = userrep.findByEmail(email);
		UserDetailsBuilder builder = null;

		if (usuario == null) {
			User document = userService.findUserByDocument(email);
			System.out.println("ACIMA" + email);
			if (document != null) {
				System.out.println("AQUIoioioioioioioioio" + document.getPassword());
				return new UsuarioSistema(document.getName(), document.getEmail(), document.getPassword(),
						authorities(document));

			}
			throw new UsernameNotFoundException("Usuário não encontrado!");
		}
		System.out.println("--------____NAO ME CHAMOU");
		// System.out.println("EPAAAAAAA" + my.getPassword());
		// return new MyUserPrincipal(usuario);
		return new UsuarioSistema(usuario.getName(), usuario.getEmail(), usuario.getPassword(), authorities(usuario));
	}

	public Collection<? extends GrantedAuthority> authorities(User usuario) {
		return authorities(rolerep.findByUsuariosIn(usuario));
	}

	public Collection<? extends GrantedAuthority> authorities(List<Role> roles) {
		Collection<GrantedAuthority> auths = new ArrayList<>();
		for (Role role : roles) {
			auths.add(new SimpleGrantedAuthority("ROLE_" + role.getRole()));
		}
		return auths;
	}
}