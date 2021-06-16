package com.krukovska.paymentsystem.service.impl;

import com.krukovska.paymentsystem.persistence.model.User;
import com.krukovska.paymentsystem.persistence.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository repo;

    @InjectMocks
    private UserServiceImpl service;

    @Test
    void findUserByEmailSuccess() {
        service.findUserByEmail("email@gmail.com");
        verify(repo, times(1)).findUserByEmailIgnoreCase("email@gmail.com");
    }

    @Test
    void loadUserByEmailNotFound() {
        when(repo.findUserByEmailIgnoreCase(anyString())).thenReturn(null);

        Exception exception = assertThrows(UsernameNotFoundException.class, () ->
                service.loadUserByUsername("username")
        );
        assertThat(exception.getMessage(), equalTo("User not found"));
    }

    @Test
    void loadUserSuccess() {
        User user = new User();
        user.setId(1L);
        when(repo.findUserByEmailIgnoreCase(anyString())).thenReturn(user);

        User result = (User) service.loadUserByUsername("username");
        assertEquals(1L, result.getId());
    }

}