package com.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.model.PasswordResetToken;
import com.example.repository.TokenRepository;
import com.example.repository.UserRepository;

@Service("tokenService")
public class TokenServiceImpl implements TokenService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private TokenRepository tokeRepository;

	@Override
	public PasswordResetToken findByToken(String token) {
		PasswordResetToken t = tokeRepository.findByToken(token);
		return t;
	}

	public void saveToken(PasswordResetToken pt) {
		tokeRepository.save(pt);
	}

	public PasswordResetToken hasToken(long id) {
		return tokeRepository.hasToken(id);
	}

}
