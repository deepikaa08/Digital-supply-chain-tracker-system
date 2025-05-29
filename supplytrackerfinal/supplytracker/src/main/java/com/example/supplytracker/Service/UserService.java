package com.example.supplytracker.Service;

import com.example.supplytracker.DTO.UserDTO;
import com.example.supplytracker.Entity.User;
import com.example.supplytracker.Enums.Role;
import com.example.supplytracker.Exceptions.EmailAlreadyExistsException;
import com.example.supplytracker.Exceptions.UserNotFoundException;
import com.example.supplytracker.Repository.UserRepo;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepo userRepository;

    @Autowired
    public UserService(UserRepo userRepository) {
        this.userRepository = userRepository;
    }

    // Registers a new user, assigns ADMIN role if it's the first user
    public User registerUser(UserDTO userDTO) throws EmailAlreadyExistsException {
        if (userRepository.existsByEmail(userDTO.getEmail())) {
            throw new EmailAlreadyExistsException("Email " + userDTO.getEmail() + " already exists");
        }

        Role assignedRole = userRepository.count() == 0 ? Role.ADMIN : null;

        User user = mapToEntity(userDTO, assignedRole);
        return userRepository.save(user);
    }

    // Validates user credentials for login
    public Optional<User> login(String email, String password) {
        return userRepository.findByEmail(email)
                .filter(user -> user.getPassword().equals(password)); // Plain text comparison (not secure)
    }

    // Retrieves all users
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // Updates a user's role by ID (throws exception if user not found)
    public void updateUserRole(Long id, Role newRole) throws UserNotFoundException {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User with id " + id + " not found"));
        user.setRole(newRole);
        userRepository.save(user);
    }

    // Converts DTO to Entity and assigns role
    private User mapToEntity(UserDTO dto, Role role) {
        User user = new User();
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword());
        user.setRole(role);
        return user;
    }

    // Converts Entity to DTO (not currently used)
    private UserDTO mapToDTO(User user) {
        return new UserDTO(user.getName(), user.getEmail(), user.getPassword());
    }
}
