package com.example;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Component
public class SuccessHandler implements AuthenticationSuccessHandler {

	private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

	@Override
	public void onAuthenticationSuccess(HttpServletRequest arg0, HttpServletResponse arg1,
			Authentication authentication) throws IOException, ServletException {
		System.out.println("FUI CHAMDO");
		boolean hasUserRole = authentication.getAuthorities().stream()
				.anyMatch(r -> r.getAuthority().equals("ROLE_USER"));
		if (hasUserRole)
			redirectStrategy.sendRedirect(arg0, arg1, "/user/home");

	}

}
