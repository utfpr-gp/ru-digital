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

	public void deleteUser(long id) {
		User user = getOne(id);
		System.out.println("DELETE USER!!!!!!!!!!!!");

		System.out.println("NO MEIO --------------------------------------------");
		Set<Role> roles = new HashSet<Role>();
		List<Role> ro = roleRepository.findAll();
		List<User> users = userRepository.findAll();
		System.out.println("ANTES DO FOR");

		for (User u : users) {
			if (u.getId() == id) {
				for (Role s : ro) {
					System.out.println("DENTRO DO FOR");
					if (s.getRole().equals("MANAGER")) {
						roles.remove(s);
						user.setActive(1);
						System.out.println("TEM MANAGER");
					}
				}
			}
		}
		user.setRoles(roles);
		updateUser(user);
		for (User u : users) {
			if (u.getId() == id) {
				userRepository.delete(u);
			}
		}
		System.out.println("DELETE USER!!!!!!!!!!!!");

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

	public List<User> findManager() {
		System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>.");
		List<User> z = manager.createQuery("select p from User p join p.roles c where c.id = 3").getResultList();
		for (int i = 0; i < z.size(); i++) {
			System.out.println("EPRA");
			System.out.println("AAAAA" + z.get(i).getName());
		}

		return z;
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
		x.setEmail(user.getEmail());
		x.setDocument(user.getDocument());
		x.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
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
		System.out.println("ANTES DO FOR");
		for (Role s : ro) {
			System.out.println("DENTRO DO FOR");
			if (s.getRole().equals("MANAGER")) {
				roles.add(s);
				user.setActive(1);
				System.out.println("TEM MANAGER");
			}
		}
		System.out.println("DEPOIS DO FOR");
		user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
		user.setRoles(roles);
		System.out.println("ROLES" + user.getRoles());
		if (user.getActive() == 1)
			userRepository.save(user);
	}

	@Override
	public User getOne(long id) {
		return userRepository.findOne(id);
	}

}
