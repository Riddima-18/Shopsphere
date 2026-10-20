package com.shopsphere.service;

import com.shopsphere.dao.CartDAO;
import com.shopsphere.dao.CouponDAO;
import com.shopsphere.dao.OrderDAO;
import com.shopsphere.model.CartItem;
import com.shopsphere.model.Coupon;
import com.shopsphere.model.User;
import com.shopsphere.util.DBUtil;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class CheckoutService {

    private final CartDAO cartDAO = new CartDAO();
    private final OrderDAO orderDAO = new OrderDAO();
    private final CouponDAO couponDAO = new CouponDAO();
    private final CouponService couponService = new CouponService();

    // Returns the new order ID
    public long placeOrder(User user, String line, String city, String state,
                           String pincode, String paymentMethod, String couponCode) throws SQLException {
        String cleanLine = Objects.toString(line, "").trim();
        String cleanCity = Objects.toString(city, "").trim();
        String cleanState = Objects.toString(state, "").trim();
        String cleanPin = Objects.toString(pincode, "").trim();

        if (cleanLine.isEmpty() || cleanCity.isEmpty() || cleanState.isEmpty()) {
            throw new IllegalArgumentException("Please fill in all address fields.");
        }
        if (!cleanPin.matches("\\d{6}")) {
            throw new IllegalArgumentException("Pincode must be 6 digits.");
        }
        if (!"COD".equals(paymentMethod) && !"CARD".equals(paymentMethod)) {
            throw new IllegalArgumentException("Choose a payment method.");
        }

        int userId = user.getUserId();
        List<CartItem> items = cartDAO.findByUser(userId);
        if (items.isEmpty()) {
            throw new IllegalArgumentException("Your cart is empty.");
        }

        // Subtotal and discount are calculated on the server from database prices
        double subtotal = 0;
        for (CartItem item : items) {
            subtotal += item.getLineTotal();
        }
        subtotal = Math.round(subtotal * 100) / 100.0;

        Coupon coupon = null;
        double discount = 0;
        String code = Objects.toString(couponCode, "").trim();
        if (!code.isEmpty()) {
            coupon = couponService.validate(code, userId, subtotal);
            discount = couponService.discountFor(coupon, subtotal);
        }
        double total = Math.round((subtotal - discount) * 100) / 100.0;

        // Mock payment: card payments are marked PAID, cash on delivery stays PENDING
        boolean card = "CARD".equals(paymentMethod);
        String paymentStatus = card ? "PAID" : "PENDING";
        String reference = card ? "MOCK-" + UUID.randomUUID() : null;

        try (Connection con = DBUtil.getConnection()) {
            con.setAutoCommit(false);
            try {
                if (coupon != null) {
                    // Re-check inside the transaction, after locking the coupon row
                    couponDAO.lockCoupon(con, coupon.getCouponId());
                    if (couponDAO.usageCount(con, coupon.getCouponId(), userId) > 0) {
                        throw new IllegalArgumentException("You have already used this coupon.");
                    }
                }

                long addressId = orderDAO.insertAddress(con, userId,
                        cleanLine, cleanCity, cleanState, cleanPin);
                long orderId = orderDAO.insertOrder(con, userId, addressId,
                        total, discount, paymentMethod, paymentStatus);

                for (CartItem item : items) {
                    if (!orderDAO.reduceStock(con, item.getProductId(), item.getQuantity())) {
                        throw new IllegalArgumentException("Not enough stock for " + item.getName() + ".");
                    }
                    orderDAO.insertOrderItem(con, orderId, item.getProductId(), item.getQuantity(),
                            Math.round(item.getFinalPrice() * 100) / 100.0);
                }

                orderDAO.insertPayment(con, orderId, paymentMethod, paymentStatus, total, reference);
                if (coupon != null) {
                    couponDAO.insertUsage(con, coupon.getCouponId(), userId, orderId);
                }
                orderDAO.clearCart(con, userId);
                con.commit();
                return orderId;
            } catch (SQLException | RuntimeException e) {
                con.rollback();   // undo everything, including the coupon usage
                throw e;
            }
        }
    }
}