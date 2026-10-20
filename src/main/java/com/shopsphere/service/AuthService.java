package com.shopsphere.service;

import com.shopsphere.dao.UserDAO;
import com.shopsphere.model.User;
import java.sql.SQLException;
import java.util.Objects;
import org.mindrot.jbcrypt.BCrypt;

public class AuthService {

    private final UserDAO userDAO = new UserDAO();

    public void register(String name, String email, String password, String mobile)
            throws SQLException {
        String cleanName = Objects.toString(name, "").trim();
        String cleanEmail = Objects.toString(email, "").trim().toLowerCase();
        String cleanPassword = Objects.toString(password, "");

        if (cleanName.isEmpty()) {
            throw new IllegalArgumentException("Name is required.");
        }
        if (!cleanEmail.contains("@")) {
            throw new IllegalArgumentException("Enter a valid email address.");
        }
        if (cleanPassword.length() < 6) {
            throw new IllegalArgumentException("Password must be at least 6 characters.");
        }
        if (userDAO.findByEmail(cleanEmail) != null) {
            throw new IllegalArgumentException("An account with this email already exists.");
        }

        User user = new User();
        user.setName(cleanName);
        user.setEmail(cleanEmail);
        user.setPassword(BCrypt.hashpw(cleanPassword, BCrypt.gensalt()));
        user.setMobile(Objects.toString(mobile, "").trim());
        user.setRole("CUSTOMER");
        userDAO.insert(user);
    }

    // Returns the user if the credentials are valid, otherwise null
    public User login(String email, String password) throws SQLException {
        String cleanEmail = Objects.toString(email, "").trim().toLowerCase();
        String cleanPassword = Objects.toString(password, "");

        User user = userDAO.findByEmail(cleanEmail);
        if (user == null || !user.isStatus()) {
            return null;
        }
        return BCrypt.checkpw(cleanPassword, user.getPassword()) ? user : null;
    }
}