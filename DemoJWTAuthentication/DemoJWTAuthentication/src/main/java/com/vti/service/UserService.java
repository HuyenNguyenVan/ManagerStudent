package com.vti.service;

import com.vti.entity.Role;
import com.vti.entity.User;
import com.vti.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Primary
public class UserService implements IUserService {

    @Autowired
    private UserRepository repository;


    public List<User> getAllUsers() {
        return repository.findAll();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Check user exists by username
        User user = repository.findByEmail(username);

        if (user == null) {
            throw new UsernameNotFoundException(username);
        }
        UserDetailsImpl userDetails = new UserDetailsImpl();

        userDetails.setId(user.getId());

        userDetails.setUsername(user.getUserName());

        userDetails.setPassword(user.getPassword());

        userDetails.setEmail(user.getEmail());

//        userDetails.setAuthorities(AuthorityUtils.createAuthorityList(
//                user.getRoles().stream().collect(
//                Collectors.joining("-", "{", "}"))
//                )
//
//        );   userDetails.setAuthorities(AuthorityUtils.createAuthorityList(user.getRoles().toString()));
//        String roles = userDetails.getAuthorities().stream().map(item -> item.getAuthority())
//                .collect(Collectors.joining("-", "{", "}"));

//        userDetails.setAuthorities(AuthorityUtils.createAuthorityList(roles));
        StringBuffer roleName = new StringBuffer();

        for (Role role : user.getRoles()) {
            if (user.getRoles().size() > 1) {
                roleName.append(role.getName() + "-");
            }
            roleName.append(role.getName());
        }

        userDetails.setAuthorities(AuthorityUtils.createAuthorityList(roleName.toString()));

        return userDetails;

//		 new org.springframework.security.core.userdetails.User(user.getUserName(), user.getPassword(),
//				AuthorityUtils.createAuthorityList(roles));
    }


}
