package com.shopsphere.dao;

import com.shopsphere.model.Coupon;
import com.shopsphere.util.DBUtil;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CouponDAO {

    public Coupon findByCode(String code) throws SQLException {
        String sql = "SELECT * FROM coupons WHERE code = ?";
        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, code);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? map(rs) : null;
            }
        }
    }

    public List<Coupon> findAll() throws SQLException {
        String sql = "SELECT * FROM coupons ORDER BY coupon_id DESC";
        List<Coupon> list = new ArrayList<>();
        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(map(rs));
            }
        }
        return list;
    }

    public void insert(Coupon c) throws SQLException {
        String sql = "INSERT INTO coupons (code, discount_type, discount_value, minimum_order, " +
                     "maximum_discount, expiry_date, status) VALUES (?, ?, ?, ?, ?, ?, TRUE)";
        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, c.getCode());
            ps.setString(2, c.getDiscountType());
            ps.setDouble(3, c.getDiscountValue());
            ps.setDouble(4, c.getMinimumOrder());
            if (c.getMaximumDiscount() == null) {
                ps.setNull(5, Types.DECIMAL);
            } else {
                ps.setDouble(5, c.getMaximumDiscount());
            }
            ps.setDate(6, c.getExpiryDate() == null ? null : Date.valueOf(c.getExpiryDate()));
            ps.executeUpdate();
        }
    }

    public void setStatus(int couponId, boolean status) throws SQLException {
        String sql = "UPDATE coupons SET status = ? WHERE coupon_id = ?";
        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setBoolean(1, status);
            ps.setInt(2, couponId);
            ps.executeUpdate();
        }
    }

    // Used by the customer-side check before checkout
    public boolean hasUsed(int couponId, int userId) throws SQLException {
        try (Connection con = DBUtil.getConnection()) {
            return usageCount(con, couponId, userId) > 0;
        }
    }

    // Locks the coupon row until the transaction ends, so two orders can't both use it
    public void lockCoupon(Connection con, int couponId) throws SQLException {
        String sql = "SELECT coupon_id FROM coupons WHERE coupon_id = ? FOR UPDATE";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, couponId);
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
            }
        }
    }

    public int usageCount(Connection con, int couponId, int userId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM coupon_usage WHERE coupon_id = ? AND user_id = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, couponId);
            ps.setInt(2, userId);
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                return rs.getInt(1);
            }
        }
    }

    public void insertUsage(Connection con, int couponId, int userId, long orderId) throws SQLException {
        String sql = "INSERT INTO coupon_usage (coupon_id, user_id, order_id) VALUES (?, ?, ?)";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, couponId);
            ps.setInt(2, userId);
            ps.setLong(3, orderId);
            ps.executeUpdate();
        }
    }

    private Coupon map(ResultSet rs) throws SQLException {
        Coupon c = new Coupon();
        c.setCouponId(rs.getInt("coupon_id"));
        c.setCode(rs.getString("code"));
        c.setDiscountType(rs.getString("discount_type"));
        c.setDiscountValue(rs.getDouble("discount_value"));
        c.setMinimumOrder(rs.getDouble("minimum_order"));
        double max = rs.getDouble("maximum_discount");
        c.setMaximumDiscount(rs.wasNull() ? null : max);
        Date expiry = rs.getDate("expiry_date");
        c.setExpiryDate(expiry == null ? null : expiry.toLocalDate());
        c.setStatus(rs.getBoolean("status"));
        return c;
    }
}