package com.shopsphere.service;

import com.shopsphere.dao.CouponDAO;
import com.shopsphere.model.Coupon;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Objects;

public class CouponService {

    private final CouponDAO couponDAO = new CouponDAO();

    // Checks a code the customer typed and returns the coupon if it can be used
    public Coupon validate(String code, int userId, double subtotal) throws SQLException {
        String clean = Objects.toString(code, "").trim().toUpperCase();
        if (clean.isEmpty()) {
            throw new IllegalArgumentException("Enter a coupon code.");
        }

        Coupon c = couponDAO.findByCode(clean);
        if (c == null || !c.isStatus()) {
            throw new IllegalArgumentException("This coupon is not valid.");
        }
        if (c.getExpiryDate() != null && c.getExpiryDate().isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("This coupon has expired.");
        }
        if (subtotal < c.getMinimumOrder()) {
            throw new IllegalArgumentException(
                    String.format("Minimum order for this coupon is Rs. %.2f.", c.getMinimumOrder()));
        }
        if (couponDAO.hasUsed(c.getCouponId(), userId)) {
            throw new IllegalArgumentException("You have already used this coupon.");
        }
        return c;
    }

    // Percent discounts respect the cap; the discount never exceeds the subtotal
    public double discountFor(Coupon c, double subtotal) {
        double discount = "PERCENT".equals(c.getDiscountType())
                ? subtotal * c.getDiscountValue() / 100
                : c.getDiscountValue();
        if (c.getMaximumDiscount() != null && discount > c.getMaximumDiscount()) {
            discount = c.getMaximumDiscount();
        }
        if (discount > subtotal) {
            discount = subtotal;
        }
        return Math.round(discount * 100) / 100.0;
    }

    // Used by the admin page
    public void create(Coupon c) throws SQLException {
        String code = Objects.toString(c.getCode(), "").trim().toUpperCase();
        if (!code.matches("[A-Z0-9_-]{3,50}")) {
            throw new IllegalArgumentException("Code must be 3 to 50 letters, numbers, - or _.");
        }
        String type = c.getDiscountType();
        if (!"PERCENT".equals(type) && !"FLAT".equals(type)) {
            throw new IllegalArgumentException("Choose a discount type.");
        }
        if (c.getDiscountValue() <= 0) {
            throw new IllegalArgumentException("Discount must be greater than 0.");
        }
        if ("PERCENT".equals(type) && c.getDiscountValue() > 100) {
            throw new IllegalArgumentException("A percent discount cannot be more than 100.");
        }
        if (c.getMinimumOrder() < 0) {
            throw new IllegalArgumentException("Minimum order cannot be negative.");
        }
        if (c.getMaximumDiscount() != null && c.getMaximumDiscount() <= 0) {
            throw new IllegalArgumentException("Maximum discount must be greater than 0.");
        }
        if (c.getExpiryDate() != null && c.getExpiryDate().isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Expiry date must be today or later.");
        }
        if (couponDAO.findByCode(code) != null) {
            throw new IllegalArgumentException("A coupon with this code already exists.");
        }
        c.setCode(code);
        couponDAO.insert(c);
    }
}