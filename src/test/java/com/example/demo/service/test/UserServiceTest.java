package com.example.demo.service.test;

import com.example.demo.entity.User;
import com.example.demo.states.UserRole;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(100L);
        testUser.setName("Test");
        testUser.setEmail("test@example.com");
        testUser.setRole(UserRole.ADMIN);
    }

    @Test
    void createUser_Success() {
        when(userRepository.existsByEmail("test@example.com")).thenReturn(false);
        when(userRepository.save(testUser)).thenReturn(testUser);
        User created = userService.createUser(testUser);
        assertEquals("Test", created.getName());
        verify(userRepository).save(testUser);
    }

    @Test
    void createUser_EmailAlreadyExists() {
        when(userRepository.existsByEmail("test@example.com")).thenReturn(true);
        assertThrows(IllegalArgumentException.class, () -> userService.createUser(testUser));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void getUser_Success() {
        when(userRepository.findById(100L)).thenReturn(Optional.of(testUser));

        User found = userService.getUser(100L);
        assertEquals("Test", found.getName());
    }

    @Test
    void getUser_NotFound() {
        when(userRepository.findById(999L)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> userService.getUser(999L));
    }
}