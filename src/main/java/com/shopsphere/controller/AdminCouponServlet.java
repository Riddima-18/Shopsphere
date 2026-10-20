package com.shopsphere.controller;

import com.shopsphere.dao.CouponDAO;
import com.shopsphere.model.Coupon;
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
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

@WebServlet("/admin/coupons")
public class AdminCouponServlet extends HttpServlet {

    private final CouponService couponService = new CouponService();
    private final CouponDAO couponDAO = new CouponDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            req.setAttribute("coupons", couponDAO.findAll());
            req.getRequestDispatcher("/admin/coupons.jsp").forward(req, resp);
        } catch (SQLException e) {
            throw new ServletException("Could not load coupons", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String action = req.getParameter("action");
        try {
            if ("save".equals(action)) {
                couponService.create(readForm(req));
                redirectWith(req, resp, "message", "Coupon created.");

            } else if ("toggle".equals(action)) {
                boolean current = "true".equals(req.getParameter("current"));
                couponDAO.setStatus(Integer.parseInt(req.getParameter("id")), !current);
                redirectWith(req, resp, "message", "Coupon updated.");

            } else {
                redirectWith(req, resp, "error", "Unknown action.");
            }
        } catch (NumberFormatException e) {
            redirectWith(req, resp, "error", "Invalid coupon.");
        } catch (IllegalArgumentException e) {
            redirectWith(req, resp, "error", e.getMessage());
        } catch (SQLException e) {
            throw new ServletException("Coupon update failed", e);
        }
    }

    private Coupon readForm(HttpServletRequest req) {
        Coupon c = new Coupon();
        c.setCode(req.getParameter("code"));
        c.setDiscountType(req.getParameter("discountType"));
        c.setDiscountValue(number(req.getParameter("discountValue"), "Discount value must be a number."));

        String min = req.getParameter("minimumOrder");
        c.setMinimumOrder(isBlank(min) ? 0 : number(min, "Minimum order must be a number."));

        String max = req.getParameter("maximumDiscount");
        c.setMaximumDiscount(isBlank(max) ? null : number(max, "Maximum discount must be a number."));

        String expiry = req.getParameter("expiryDate");
        c.setExpiryDate(isBlank(expiry) ? null : date(expiry));

        c.setStatus(true);
        return c;
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }

    private double number(String value, String message) {
        try {
            return Double.parseDouble(value.trim());
        } catch (Exception e) {
            throw new IllegalArgumentException(message);
        }
    }

    private LocalDate date(String value) {
        try {
            return LocalDate.parse(value.trim());
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Expiry date is not valid.");
        }
    }

    private void redirectWith(HttpServletRequest req, HttpServletResponse resp,
                              String key, String text) throws IOException {
        resp.sendRedirect(req.getContextPath() + "/admin/coupons?" + key + "="
                + URLEncoder.encode(text, StandardCharsets.UTF_8));
    }
}