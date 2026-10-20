package com.shopsphere.dao;

import com.shopsphere.model.Order;
import com.shopsphere.model.OrderItem;
import com.shopsphere.util.DBUtil;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderDAO {

    // Joins the address so each order carries its delivery details
    private static final String ORDER_SQL =
        "SELECT o.*, a.address_line, a.city, a.state, a.pincode " +
        "FROM orders o JOIN addresses a ON o.address_id = a.address_id ";

    // Methods that take a Connection run inside the transaction started by CheckoutService

    public long insertAddress(Connection con, int userId, String line, String city,
                              String state, String pincode) throws SQLException {
        String sql = "INSERT INTO addresses (user_id, address_line, city, state, pincode) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, userId);
            ps.setString(2, line);
            ps.setString(3, city);
            ps.setString(4, state);
            ps.setString(5, pincode);
            ps.executeUpdate();
            return generatedKey(ps);
        }
    }

    public long insertOrder(Connection con, int userId, long addressId, double total,
                        double discount, String paymentMethod, String paymentStatus) throws SQLException {
    String sql = "INSERT INTO orders (user_id, address_id, total_amount, discount, payment_method, payment_status) " +
                 "VALUES (?, ?, ?, ?, ?, ?)";
    try (PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
        ps.setInt(1, userId);
        ps.setLong(2, addressId);
        ps.setDouble(3, total);
        ps.setDouble(4, discount);
        ps.setString(5, paymentMethod);
        ps.setString(6, paymentStatus);
        ps.executeUpdate();
        return generatedKey(ps);
    }
}

    public boolean reduceStock(Connection con, int productId, int quantity) throws SQLException {
        String sql = "UPDATE products SET stock = stock - ? WHERE product_id = ? AND stock >= ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, quantity);
            ps.setInt(2, productId);
            ps.setInt(3, quantity);
            return ps.executeUpdate() == 1;
        }
    }

    public void insertOrderItem(Connection con, long orderId, int productId,
                                int quantity, double unitPrice) throws SQLException {
        String sql = "INSERT INTO order_items (order_id, product_id, quantity, price) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setLong(1, orderId);
            ps.setInt(2, productId);
            ps.setInt(3, quantity);
            ps.setDouble(4, unitPrice);
            ps.executeUpdate();
        }
    }

    public void insertPayment(Connection con, long orderId, String method, String status,
                              double amount, String reference) throws SQLException {
        String sql = "INSERT INTO payments (order_id, payment_method, transaction_reference, amount, payment_status) " +
                     "VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setLong(1, orderId);
            ps.setString(2, method);
            ps.setString(3, reference);
            ps.setDouble(4, amount);
            ps.setString(5, status);
            ps.executeUpdate();
        }
    }

    public void clearCart(Connection con, int userId) throws SQLException {
        String sql = "DELETE FROM cart WHERE user_id = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.executeUpdate();
        }
    }

    public List<Order> findByUser(int userId) throws SQLException {
        String sql = ORDER_SQL + "WHERE o.user_id = ? ORDER BY o.order_id DESC";
        List<Order> list = new ArrayList<>();
        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(map(rs));
                }
            }
        }
        return list;
    }

    // The user ID check means one customer can never open another customer's order
    public Order findByIdForUser(long orderId, int userId) throws SQLException {
        String sql = ORDER_SQL + "WHERE o.order_id = ? AND o.user_id = ?";
        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setLong(1, orderId);
            ps.setInt(2, userId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? map(rs) : null;
            }
        }
    }

    public List<OrderItem> findItems(long orderId) throws SQLException {
        String sql = "SELECT oi.product_id, oi.quantity, oi.price, p.name " +
                     "FROM order_items oi JOIN products p ON oi.product_id = p.product_id " +
                     "WHERE oi.order_id = ? ORDER BY oi.order_item_id";
        List<OrderItem> items = new ArrayList<>();
        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setLong(1, orderId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    OrderItem item = new OrderItem();
                    item.setProductId(rs.getInt("product_id"));
                    item.setQuantity(rs.getInt("quantity"));
                    item.setPrice(rs.getDouble("price"));
                    item.setName(rs.getString("name"));
                    items.add(item);
                }
            }
        }
        return items;
    }

    private long generatedKey(PreparedStatement ps) throws SQLException {
        try (ResultSet keys = ps.getGeneratedKeys()) {
            keys.next();
            return keys.getLong(1);
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
        return o;
    }
}