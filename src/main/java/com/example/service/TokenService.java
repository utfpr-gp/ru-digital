package com.example.service;

import com.example.model.PasswordResetToken;

public interface TokenService {
	public PasswordResetToken findByToken(String token);

	public void saveToken(PasswordResetToken pt);

	public PasswordResetToken hasToken(long id);

}
