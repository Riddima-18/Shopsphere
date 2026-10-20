package com.shopsphere.controller;

import com.shopsphere.service.AdminOrderService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;

@WebServlet("/admin/orders")
public class AdminOrderServlet extends HttpServlet {

    private final AdminOrderService orderService = new AdminOrderService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            req.setAttribute("orders", orderService.listOrders());
            req.getRequestDispatcher("/admin/orders.jsp").forward(req, resp);
        } catch (SQLException e) {
            throw new ServletException("Could not load orders", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        if (!"status".equals(req.getParameter("action"))) {
            redirectWith(req, resp, "error", "Unknown action.");
            return;
        }
        try {
            long orderId = Long.parseLong(req.getParameter("orderId"));
            String status = req.getParameter("status");
            orderService.updateStatus(orderId, status);
            redirectWith(req, resp, "message", "Order #" + orderId + " is now " + status + ".");
        } catch (NumberFormatException e) {
            redirectWith(req, resp, "error", "Invalid order.");
        } catch (IllegalArgumentException e) {
            redirectWith(req, resp, "error", e.getMessage());
        } catch (SQLException e) {
            throw new ServletException("Order update failed", e);
        }
    }

    private void redirectWith(HttpServletRequest req, HttpServletResponse resp,
                              String key, String text) throws IOException {
        resp.sendRedirect(req.getContextPath() + "/admin/orders?" + key + "="
                + URLEncoder.encode(text, StandardCharsets.UTF_8));
    }
}