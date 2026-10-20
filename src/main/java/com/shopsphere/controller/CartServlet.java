package com.shopsphere.controller;

import com.shopsphere.model.CartItem;
import com.shopsphere.model.User;
import com.shopsphere.service.CartService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/cart")
public class CartServlet extends HttpServlet {

    private final CartService cartService = new CartService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        User user = (User) req.getSession().getAttribute("user");
        try {
            List<CartItem> items = cartService.getCart(user.getUserId());
            req.setAttribute("cartItems", items);
            req.setAttribute("cartTotal", cartService.calculateTotal(items));
            req.getRequestDispatcher("/cart.jsp").forward(req, resp);
        } catch (SQLException e) {
            throw new ServletException("Could not load cart", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        User user = (User) req.getSession().getAttribute("user");
        String action = req.getParameter("action");
        try {
            int productId = Integer.parseInt(req.getParameter("productId"));
            int quantity = parseQuantity(req.getParameter("quantity"));

            String message;
            switch (action == null ? "" : action) {
                case "add" -> {
                    cartService.add(user.getUserId(), productId, quantity);
                    message = "Added to cart.";
                }
                case "update" -> {
                    cartService.updateQuantity(user.getUserId(), productId, quantity);
                    message = quantity == 0 ? "Item removed." : "Quantity updated.";
                }
                case "remove" -> {
                    cartService.remove(user.getUserId(), productId);
                    message = "Item removed.";
                }
                default -> throw new IllegalArgumentException("Unknown cart action.");
            }
            redirectWith(req, resp, "message", message);

        } catch (NumberFormatException e) {
            redirectWith(req, resp, "error", "Invalid product or quantity.");
        } catch (IllegalArgumentException e) {
            redirectWith(req, resp, "error", e.getMessage());
        } catch (SQLException e) {
            throw new ServletException("Cart update failed", e);
        }
    }

    // Blank quantity means 1
    private int parseQuantity(String value) {
        return (value == null || value.isBlank()) ? 1 : Integer.parseInt(value.trim());
    }

    // Post/Redirect/Get: the cart page reloads cleanly and refresh won't resubmit
    private void redirectWith(HttpServletRequest req, HttpServletResponse resp,
                              String key, String text) throws IOException {
        resp.sendRedirect(req.getContextPath() + "/cart?" + key + "="
                + URLEncoder.encode(text, StandardCharsets.UTF_8));
    }
}