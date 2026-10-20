package com.shopsphere.controller;

import com.shopsphere.model.CartItem;
import com.shopsphere.model.Coupon;
import com.shopsphere.model.User;
import com.shopsphere.service.CartService;
import com.shopsphere.service.CheckoutService;
import com.shopsphere.service.CouponService;
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

@WebServlet("/checkout")
public class CheckoutServlet extends HttpServlet {

    private final CartService cartService = new CartService();
    private final CheckoutService checkoutService = new CheckoutService();
    private final CouponService couponService = new CouponService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        User user = (User) req.getSession().getAttribute("user");
        try {
            if (cartService.getCart(user.getUserId()).isEmpty()) {
                resp.sendRedirect(req.getContextPath() + "/cart?error="
                        + URLEncoder.encode("Your cart is empty.", StandardCharsets.UTF_8));
                return;
            }
            showForm(req, resp, user, null);
        } catch (SQLException e) {
            throw new ServletException("Could not load checkout", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        User user = (User) req.getSession().getAttribute("user");
        try {
            // "Apply" only recalculates the summary; nothing is saved
            if ("apply".equals(req.getParameter("action"))) {
                showForm(req, resp, user, null);
                return;
            }

            long orderId = checkoutService.placeOrder(user,
                    req.getParameter("line"), req.getParameter("city"),
                    req.getParameter("state"), req.getParameter("pincode"),
                    req.getParameter("paymentMethod"), req.getParameter("coupon"));
            resp.sendRedirect(req.getContextPath() + "/orders?placed=" + orderId);
        } catch (IllegalArgumentException e) {
            try {
                showForm(req, resp, user, e.getMessage());
            } catch (SQLException ex) {
                throw new ServletException("Could not load checkout", ex);
            }
        } catch (SQLException e) {
            throw new ServletException("Order failed", e);
        }
    }

    private void showForm(HttpServletRequest req, HttpServletResponse resp,
                          User user, String error) throws SQLException, ServletException, IOException {
        List<CartItem> items = cartService.getCart(user.getUserId());
        double subtotal = cartService.calculateTotal(items);

        double discount = 0;
        String appliedCode = null;
        String couponError = null;
        String code = req.getParameter("coupon");
        if (code != null && !code.isBlank()) {
            try {
                Coupon coupon = couponService.validate(code, user.getUserId(), subtotal);
                discount = couponService.discountFor(coupon, subtotal);
                appliedCode = coupon.getCode();
            } catch (IllegalArgumentException e) {
                couponError = e.getMessage();
            }
        }

        req.setAttribute("cartItems", items);
        req.setAttribute("subtotal", subtotal);
        req.setAttribute("discount", discount);
        req.setAttribute("finalTotal", Math.round((subtotal - discount) * 100) / 100.0);
        req.setAttribute("appliedCode", appliedCode);
        req.setAttribute("error", error != null ? error : couponError);
        req.getRequestDispatcher("/checkout.jsp").forward(req, resp);
    }
}