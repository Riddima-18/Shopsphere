package com.shopsphere.swing;

import com.shopsphere.model.User;
import com.shopsphere.service.AuthService;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.sql.SQLException;
import javax.swing.*;

public class LoginFrame extends JFrame {

    private final JTextField emailField = new JTextField(20);
    private final JPasswordField passwordField = new JPasswordField(20);
    private final JCheckBox showPassword = new JCheckBox("Show password");
    private final JButton loginButton = new JButton("Login");
    private final JLabel statusLabel = new JLabel(" ");

    private final AuthService authService = new AuthService();

    public LoginFrame() {
        super("ShopSphere Admin - Login");
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel form = new JPanel(new GridLayout(4, 2, 8, 8));
        form.setBorder(BorderFactory.createEmptyBorder(16, 16, 8, 16));
        form.add(new JLabel("Email:"));
        form.add(emailField);
        form.add(new JLabel("Password:"));
        form.add(passwordField);
        form.add(new JLabel(""));
        form.add(showPassword);
        form.add(new JLabel(""));
        form.add(loginButton);

        statusLabel.setForeground(java.awt.Color.RED);
        statusLabel.setBorder(BorderFactory.createEmptyBorder(0, 16, 12, 16));

        setLayout(new BorderLayout());
        add(form, BorderLayout.CENTER);
        add(statusLabel, BorderLayout.SOUTH);

        // Show or hide the password characters
        showPassword.addActionListener(e ->
                passwordField.setEchoChar(showPassword.isSelected() ? (char) 0 : '*'));

        // Pressing Enter in the password field also logs in
        passwordField.addActionListener(e -> attemptLogin());
        loginButton.addActionListener(e -> attemptLogin());

        pack();
        setLocationRelativeTo(null);
    }

    private void attemptLogin() {
        String email = emailField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (email.isEmpty() || password.isEmpty()) {
            statusLabel.setText("Enter your email and password.");
            return;
        }

        try {
            User user = authService.login(email, password);
            if (user == null) {
                statusLabel.setText("Invalid email or password.");
                passwordField.setText("");
                return;
            }
            if (!"ADMIN".equals(user.getRole())) {
                // Same rule as the web admin filter
                statusLabel.setText("This account is not an admin.");
                return;
            }
            // Open the dashboard and close the login window
            new AdminDashboardFrame(user).setVisible(true);
            dispose();
        } catch (SQLException ex) {
            statusLabel.setText("Database error: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {
            // Fall back to the default look and feel
        }
        SwingUtilities.invokeLater(() -> new LoginFrame().setVisible(true));
    }
}