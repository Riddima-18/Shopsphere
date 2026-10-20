package com.shopsphere.controller;

import com.shopsphere.dao.DashboardDAO;
import com.shopsphere.dao.ProductDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/admin/dashboard")
public class AdminDashboardServlet extends HttpServlet {

    private final DashboardDAO dashboardDAO = new DashboardDAO();
    private final ProductDAO productDAO = new ProductDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            req.setAttribute("productCount", dashboardDAO.countProducts());
            req.setAttribute("orderCount", dashboardDAO.countOrders());
            req.setAttribute("revenue", dashboardDAO.paidRevenue());
            req.setAttribute("lowStock", productDAO.findLowStock(5));
            req.setAttribute("activeSessions", getServletContext().getAttribute("activeSessions"));
            req.getRequestDispatcher("/admin/dashboard.jsp").forward(req, resp);
        } catch (SQLException e) {
            throw new ServletException("Could not load dashboard", e);
        }
    }
}