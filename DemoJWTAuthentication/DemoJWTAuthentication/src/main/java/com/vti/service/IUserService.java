package com.vti.service;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.vti.entity.User;

public interface IUserService extends UserDetailsService {

	List<User> getAllUsers();
}
