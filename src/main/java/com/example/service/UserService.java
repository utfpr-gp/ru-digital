package com.example.service;

import java.util.List;

import com.example.model.User;

public interface UserService {
	public User findUserByEmail(String email);

	public User findUserByDocument(String document);

	public User getOne(long id);

	public List<User> findAll();

	public void saveUser(User user);

	public void updateUser(User user);

	public void saveManager(User user);

	public void deleteUser(long id);

	public List<User> findManager();

	public void saveUserManager(User user, String confirm);

	public void updateManager(User user, String confirm);
}
