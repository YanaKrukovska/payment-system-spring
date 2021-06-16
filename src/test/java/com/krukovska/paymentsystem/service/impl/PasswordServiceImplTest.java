package com.krukovska.paymentsystem.service.impl;

import com.krukovska.paymentsystem.service.PasswordService;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;


class PasswordServiceImplTest {

    private final PasswordService passwordService = new PasswordServiceImpl(new BCryptPasswordEncoder());

    @Test
    void encodePasswordNotEqualsEmpty() {
        String result = passwordService.encodePassword("password");
        assertThat(passwordService.compareRawAndEncodedPassword("", result)).isFalse();
    }

    @Test
    void encodePasswordEqualsRaw() {
        String result = passwordService.encodePassword("password");
        assertThat(passwordService.compareRawAndEncodedPassword("password", result)).isTrue();
    }
}