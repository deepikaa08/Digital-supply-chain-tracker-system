package com.example.supplytracker.Config;

import com.example.supplytracker.Entity.User;
import lombok.Data;
import lombok.Getter;

@Data
public class SessionManager {

    @Getter
    private static User currentUser; // Stores the currently logged-in user

    public static void login(User user) {
        currentUser = user; // Set the logged-in user
    }

    public static void logout() {
        currentUser = null; // Clear the user on logout
    }

    public static boolean isLoggedIn() {
        return currentUser != null; // Check if a user is logged in
    }
}
