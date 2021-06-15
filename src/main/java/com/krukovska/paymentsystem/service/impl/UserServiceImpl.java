package com.krukovska.paymentsystem.service.impl;

import com.krukovska.paymentsystem.persistence.model.User;
import com.krukovska.paymentsystem.persistence.repository.UserRepository;
import com.krukovska.paymentsystem.service.PasswordService;
import com.krukovska.paymentsystem.service.UserService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService, UserDetailsService {

    private final PasswordService passwordService;
    private final UserRepository userRepository;

    public UserServiceImpl(PasswordService passwordService, UserRepository userRepository) {
        this.passwordService = passwordService;
        this.userRepository = userRepository;
    }

    @Override
    public User findUserByEmail(String email) {
        return userRepository.findUserByEmailIgnoreCase(email);
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        var user = findUserByEmail(s);

        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }

        return user;
    }


}
