package com.example.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.model.User;

@Repository("userRepository")
public interface UserRepository extends JpaRepository<User, Long> {
	User findByEmail(String email);

	User findByDocument(String document);

	List<User> findAll();

	@Query("select p from User p join p.roles c where c.id = ?1")
	List<User> listManager(Integer id);

	@Query("select p from User p join p.roles c where c.id = 3")
	Page<User> findManagers(Pageable pageable);

}
