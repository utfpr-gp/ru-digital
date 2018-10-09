package com.example.configuration;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.example.Seguranca;
import com.example.SuccessHandler;
import com.example.repository.UserRepository;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
	@Autowired
	private final UserRepository userRepository;

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Autowired
	private DataSource dataSource;

	@Autowired
	private Seguranca seguranca;

	@Value("${spring.queries.users-query}")
	private String usersQuery;

	@Value("${spring.queries.roles-query}")
	private String rolesQuery;

	@Override
	protected void configure(AuthenticationManagerBuilder builder) throws Exception {
		System.out.println("CONFIGURE--");
		builder.userDetailsService(seguranca).passwordEncoder(new BCryptPasswordEncoder());

	}

	@Autowired
	private SuccessHandler successHandler;

	@Override
	protected void configure(HttpSecurity http) throws Exception {

		http.authorizeRequests().antMatchers("/").permitAll().antMatchers("/login").permitAll()

				.antMatchers("/emailexists").permitAll().antMatchers("/userexists").permitAll()
				.antMatchers("/email-send/{id}").permitAll().antMatchers("/registration").permitAll()
				.antMatchers("/manager/**").access("hasRole('ROLE_MANAGER')").antMatchers("/admin/**").hasRole("ADMIN")
				.anyRequest().authenticated().anyRequest().authenticated().and().csrf().disable().formLogin()
				.successHandler(successHandler).loginPage("/login").failureUrl("/login?error=true")
				.usernameParameter("email").passwordParameter("password").and().logout()
				.logoutRequestMatcher(new AntPathRequestMatcher("/logout")).logoutSuccessUrl("/").and()
				.exceptionHandling().accessDeniedPage("/403");

	}

	@Bean
	public PasswordEncoder encoder() {
		return new BCryptPasswordEncoder();
	}

	@Autowired
	public SecurityConfiguration(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Bean
	public DaoAuthenticationProvider getAuthenticationProvider() {
		System.out.println("--------------ESTOU SENDO INVOCADO ");
		DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
		authenticationProvider.setUserDetailsService(userDetailsService());
		authenticationProvider.setPasswordEncoder(encoder());
		return authenticationProvider;
	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/resources/**", "/static/**", "/css/**", "/js/**", "/sasscompiled/**",
				"/images/**");
	}

}
