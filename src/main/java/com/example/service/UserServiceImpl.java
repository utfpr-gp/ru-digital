package com.example.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.model.Role;
import com.example.model.User;
import com.example.repository.RoleRepository;
import com.example.repository.UserRepository;

@Service("userService")
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private RoleRepository roleRepository;
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	public User findUserByEmail(String email) {
		return userRepository.findByEmail(email);

	}

	@PersistenceContext
	private EntityManager manager;

	@Override
	public User findUserByDocument(String document) {
		return userRepository.findByDocument(document);
	}

	public List<User> findAll() {
		return userRepository.findAll();
	}

	@Override
	public void saveUser(User user) {
		Set<Role> roles = new HashSet<Role>();
		List<Role> ro = roleRepository.findAll();
		Role r = roleRepository.findByRole("USER");
		for (Role s : ro) {
			if (s.getRole().equals("USER")) {
				roles.add(s);
				user.setActive(1);
			}
		}
		user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
		System.out.println("EIIIIiOIIIIIOOOOO" + user.getPassword());
		user.setRoles(roles);
		System.out.println("EIIIIiOIIIIIOOOOO");
		if (user.getActive() == 1)
			userRepository.save(user);
	}

	public void updateUser(User user) {
		System.out.println("-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-++-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+");
		User x = userRepository.getOne(user.getId());
		System.out.println("-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-++-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+"
				+ x.getDocument());

		x.setName(user.getName());
		userRepository.save(x);
		System.out.println("-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-++-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+");

		// manager.createQuery("update User set name = \'xxxx\' where
		// user_id=1").executeUpdate();

	}

	@Override
	public void saveManager(User user) {
		Set<Role> roles = new HashSet<Role>();
		List<Role> ro = roleRepository.findAll();
		Role r = roleRepository.findByRole("MANAGER");
		for (Role s : ro) {
			if (s.getRole().equals("MANAGER")) {
				roles.add(s);
				user.setActive(1);
			}
		}
		user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
		user.setRoles(roles);
		if (user.getActive() == 1)
			userRepository.save(user);
	}

	@Override
	public User getOne(long id) {
		return userRepository.findOne(id);
	}

}
