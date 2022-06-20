package com.vti.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vti.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {


	 User findByEmail(String name);

	 User findByUserName(String userName);

}
