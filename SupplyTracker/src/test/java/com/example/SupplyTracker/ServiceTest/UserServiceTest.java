package com.example.supplytracker.ServiceTest;




import com.example.supplytracker.DTO.UserDTO;
import com.example.supplytracker.Entity.User;
import com.example.supplytracker.Enums.Role;
import com.example.supplytracker.Exceptions.EmailAlreadyExistsException;
import com.example.supplytracker.Exceptions.UserNotFoundException;
import com.example.supplytracker.Repository.UserRepo;
import com.example.supplytracker.Service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.*;

        import static org.junit.jupiter.api.Assertions.*;
        import static org.mockito.Mockito.*;



public class UserServiceTest {

    private UserRepo userRepo;
    private UserService userService;

    @BeforeEach
    public void setup() {
        userRepo = mock(UserRepo.class);
        userService = new UserService(userRepo);
    }

    @Test
    public void testRegisterUser_FirstUser_AdminRoleAssigned() throws EmailAlreadyExistsException {
        UserDTO dto = new UserDTO("Alice", "alice@example.com", "pass123");

        when(userRepo.count()).thenReturn(0L);
        when(userRepo.existsByEmail(dto.getEmail())).thenReturn(false);
        when(userRepo.save(Mockito.any(User.class))).thenAnswer(i -> i.getArguments()[0]);

        User saved = userService.registerUser(dto);

        assertEquals(Role.ADMIN, saved.getRole());
        assertEquals(dto.getName(), saved.getName());
    }

    @Test
    public void testLogin_Successful() {
        User user = new User();
        user.setEmail("bob@example.com");
        user.setPassword("pass456");

        when(userRepo.findByEmail("bob@example.com")).thenReturn(Optional.of(user));

        Optional<User> result = userService.login("bob@example.com", "pass456");
        assertTrue(result.isPresent());
    }

    @Test
    public void testLogin_Failure() {
        when(userRepo.findByEmail("bob@example.com")).thenReturn(Optional.empty());

        Optional<User> result = userService.login("bob@example.com", "wrong");
        assertFalse(result.isPresent());
    }

    @Test
    public void testUpdateUserRole() throws UserNotFoundException {
        User user = new User();
        user.setId(1L);
        user.setRole(null);

        when(userRepo.findById(1L)).thenReturn(Optional.of(user));

        userService.updateUserRole(1L, Role.ADMIN);

        verify(userRepo).save(user);
        assertEquals(Role.ADMIN, user.getRole());
    }
}

