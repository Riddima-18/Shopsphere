package com.shopsphere.dao;

import com.shopsphere.model.Order;
import com.shopsphere.util.DBUtil;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AdminOrderDAO {

    // Joins the address and the customer name so the admin list shows who ordered
    private static final String LIST_SQL =
        "SELECT o.*, a.address_line, a.city, a.state, a.pincode, u.name AS customer_name " +
        "FROM orders o " +
        "JOIN addresses a ON o.address_id = a.address_id " +
        "JOIN users u ON o.user_id = u.user_id ";

    public List<Order> findAll() throws SQLException {
        List<Order> list = new ArrayList<>();
        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(LIST_SQL + "ORDER BY o.order_id DESC");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(map(rs));
            }
        }
        return list;
    }

    // Reads the current status and locks the row until the transaction ends,
    // so two admins can't change the same order at the same time
    public String lockStatus(Connection con, long orderId) throws SQLException {
        String sql = "SELECT order_status FROM orders WHERE order_id = ? FOR UPDATE";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setLong(1, orderId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getString(1) : null;
            }
        }
    }

    public void setStatus(Connection con, long orderId, String status) throws SQLException {
        String sql = "UPDATE orders SET order_status = ? WHERE order_id = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setLong(2, orderId);
            ps.executeUpdate();
        }
    }

    // Puts back the stock for every item in a cancelled order
    public void restoreStock(Connection con, long orderId) throws SQLException {
        String sql = "UPDATE products p JOIN order_items oi ON p.product_id = oi.product_id " +
                     "SET p.stock = p.stock + oi.quantity WHERE oi.order_id = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setLong(1, orderId);
            ps.executeUpdate();
        }
    }

    private Order map(ResultSet rs) throws SQLException {
        Order o = new Order();
        o.setOrderId(rs.getLong("order_id"));
        o.setUserId(rs.getInt("user_id"));
        o.setAddressId(rs.getLong("address_id"));
        o.setTotalAmount(rs.getDouble("total_amount"));
        o.setPaymentMethod(rs.getString("payment_method"));
        o.setPaymentStatus(rs.getString("payment_status"));
        o.setOrderStatus(rs.getString("order_status"));
        o.setCreatedAt(rs.getString("created_at"));
        o.setAddressLine(rs.getString("address_line"));
        o.setCity(rs.getString("city"));
        o.setState(rs.getString("state"));
        o.setPincode(rs.getString("pincode"));
        o.setCustomerName(rs.getString("customer_name"));
        return o;
    }
}