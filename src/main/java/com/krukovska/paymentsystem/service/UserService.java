package com.krukovska.paymentsystem.service;

import com.krukovska.paymentsystem.persistence.model.User;

public interface UserService {

    User findUserByEmail(String email);
}
