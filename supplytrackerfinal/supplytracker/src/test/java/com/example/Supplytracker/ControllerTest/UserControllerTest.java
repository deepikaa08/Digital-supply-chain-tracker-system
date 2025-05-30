package com.example.Supplytracker.ControllerTest;

import com.example.Supplytracker.Config.SessionManager;
import com.example.Supplytracker.Controller.UserController;
import com.example.Supplytracker.DTO.UserDTO;
import com.example.Supplytracker.Entity.User;
import com.example.Supplytracker.Exceptions.EmailAlreadyExistsException;
import com.example.Supplytracker.Service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserControllerTest {

    private UserService userService;
    private UserController userController;

    @BeforeEach
    public void setup() {
        userService = mock(UserService.class);  // Mock the service layer
        userController = new UserController(userService);  // Inject mock into controller
    }

    @Test
    public void testRegisterUser() throws EmailAlreadyExistsException {
        UserDTO dto = new UserDTO("Tom", "tom@example.com", "123");
        User mockUser = new User();
        mockUser.setId(100L);

        when(userService.registerUser(dto)).thenReturn(mockUser);  // Mock register response

        var response = userController.register(dto);
        assertEquals(200, response.getStatusCodeValue());  // Expect success
    }

    @Test
    public void testLoginSuccess() {
        User user = new User();
        user.setName("Bob");

        when(userService.login("bob@example.com", "pass")).thenReturn(Optional.of(user));  // Valid credentials

        var response = userController.login("bob@example.com", "pass");
        assertEquals(200, response.getStatusCodeValue());  // Expect success
    }

    @Test
    public void testLoginFailure() {
        when(userService.login("bob@example.com", "wrong")).thenReturn(Optional.empty());  // Invalid credentials

        var response = userController.login("bob@example.com", "wrong");
        assertEquals(401, response.getStatusCodeValue());  // Expect unauthorized
    }

    @Test
    public void testGetAllUsers_Unauthenticated() {
        SessionManager.logout();  // Ensure no user is logged in
        var response = userController.getAllUsers();
        assertEquals(401, response.getStatusCodeValue());  // Expect unauthorized
    }
}
