package com.shopsphere.controller;

import com.shopsphere.dao.OrderDAO;
import com.shopsphere.model.Order;
import com.shopsphere.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/orders")
public class OrderServlet extends HttpServlet {

    private final OrderDAO orderDAO = new OrderDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        User user = (User) req.getSession().getAttribute("user");
        String idParam = req.getParameter("id");
        try {
            // No id: show the order list
            if (idParam == null) {
                req.setAttribute("orders", orderDAO.findByUser(user.getUserId()));
                req.getRequestDispatcher("/orders.jsp").forward(req, resp);
                return;
            }

            // With an id: show one order, only if it belongs to this user
            long orderId = Long.parseLong(idParam);
            Order order = orderDAO.findByIdForUser(orderId, user.getUserId());
            if (order == null) {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Order not found");
                return;
            }
            req.setAttribute("order", order);
            req.setAttribute("items", orderDAO.findItems(orderId));
            req.getRequestDispatcher("/order-details.jsp").forward(req, resp);
        } catch (NumberFormatException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid order id");
        } catch (SQLException e) {
            throw new ServletException("Could not load orders", e);
        }
    }
}