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

	@Query(value = "select usuarios_user_id from user_roles where roles_role_id = ?1 and usuarios_user_id = ?2  limit 1 ", nativeQuery = true)
	long user(int role_id, long user_id);

	@Query(value = "select user_id, document, email, image, name, active, deleted, password, pin,  company_id  from user join user_roles on user.user_id = user_roles.usuarios_user_id where (roles_role_id = 3 and company_id = ?1 and user_id != ?2 and deleted IS false) ", nativeQuery = true)
	List<User> userCompany(long c, long u);

	@Query("select p from User p join p.roles c where c.id = ?1")
	List<User> listManager(Integer id);

	@Query("select p from User p join p.roles c where c.id = 3")
	Page<User> findManagers(Pageable pageable);

}
