package com.shopsphere.controller;

import com.shopsphere.service.AuthService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {

    private final AuthService authService = new AuthService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.getRequestDispatcher("/register.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            authService.register(
                req.getParameter("name"),
                req.getParameter("email"),
                req.getParameter("password"),
                req.getParameter("mobile"));
            // Post/Redirect/Get: avoids resubmitting the form on refresh
            resp.sendRedirect(req.getContextPath() + "/login?registered=true");
        } catch (IllegalArgumentException e) {
            // Request parameters are kept on forward, so the form refills automatically
            req.setAttribute("error", e.getMessage());
            req.getRequestDispatcher("/register.jsp").forward(req, resp);
        } catch (SQLException e) {
            throw new ServletException("Registration failed", e);
        }
    }
}