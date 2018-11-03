package com.example.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
		user.setDeleted(true);
		updateUser(user);

	}

	public Page<User> findManagers(Pageable pageable) {
		return userRepository.findManagers(pageable);
	}

	@Override
	public User findUserByDocument(String document) {
		return userRepository.findByDocument(document);
	}

	public List<User> findAll() {
		return userRepository.findAll();
	}

	public List<User> findManager() {
		System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>.");
		List<User> z = userRepository.listManager(3); // ARRUMAR ISSO
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

	public void updateManager(User user, String confirm) {
		System.out.println(
				"-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-++-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+" + confirm);
		System.out.println("ENCODEDE!!!!!!!!!!!!!!!!!!!!!!!!!!!" + bCryptPasswordEncoder.encode(confirm));
		User x = userRepository.getOne(user.getId());
		System.out.println("-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-++-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+"
				+ x.getPassword());
		System.out.println("MEU GET PASSWORD" + user.getPassword());
		System.out.println("MEU CONFIM" + confirm);
		x.setName(user.getName());
		x.setEmail(user.getEmail());
		x.setDocument(user.getDocument());
		if (user.getPassword() != null && !user.getPassword().equals("")) {
			System.out.println("AINDA VEM AQUI");
			x.setPassword(bCryptPasswordEncoder.encode(confirm));
		}

		userRepository.save(x);
		System.out.println("-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-++-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+");

	}

	public void updateUser(User user) {
		System.out.println("-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-++-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+"
				+ user.getPassword());
		User x = userRepository.getOne(user.getId());
		System.out.println("-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-++-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+"
				+ x.getPassword());

		x.setName(user.getName());
		x.setEmail(user.getEmail());
		x.setDocument(user.getDocument());
		if (x.getPassword() != user.getPassword())
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
	public void saveUserManager(User user, String confirm) {
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

		if (confirm != null && !confirm.equals("")) {
			user.setPassword(bCryptPasswordEncoder.encode(confirm));
		} else {

		}
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
