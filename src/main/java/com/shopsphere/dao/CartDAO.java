package com.shopsphere.dao;

import com.shopsphere.model.CartItem;
import com.shopsphere.util.DBUtil;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CartDAO {

    private static final String BASE_SQL =
        "SELECT c.product_id, c.quantity, p.name, p.price, p.discount, p.stock, p.image_url " +
        "FROM cart c JOIN products p ON c.product_id = p.product_id " +
        "WHERE c.user_id = ?";

    public List<CartItem> findByUser(int userId) throws SQLException {
        String sql = BASE_SQL + " ORDER BY c.cart_id";
        List<CartItem> items = new ArrayList<>();
        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    items.add(map(rs));
                }
            }
        }
        return items;
    }

    public CartItem findItem(int userId, int productId) throws SQLException {
        String sql = BASE_SQL + " AND c.product_id = ?";
        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setInt(2, productId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? map(rs) : null;
            }
        }
    }

    public void insert(int userId, int productId, int quantity) throws SQLException {
        String sql = "INSERT INTO cart (user_id, product_id, quantity) VALUES (?, ?, ?)";
        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setInt(2, productId);
            ps.setInt(3, quantity);
            ps.executeUpdate();
        }
    }

    public void updateQuantity(int userId, int productId, int quantity) throws SQLException {
        String sql = "UPDATE cart SET quantity = ? WHERE user_id = ? AND product_id = ?";
        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, quantity);
            ps.setInt(2, userId);
            ps.setInt(3, productId);
            ps.executeUpdate();
        }
    }

    public void delete(int userId, int productId) throws SQLException {
        String sql = "DELETE FROM cart WHERE user_id = ? AND product_id = ?";
        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setInt(2, productId);
            ps.executeUpdate();
        }
    }

    private CartItem map(ResultSet rs) throws SQLException {
        CartItem item = new CartItem();
        item.setProductId(rs.getInt("product_id"));
        item.setQuantity(rs.getInt("quantity"));
        item.setName(rs.getString("name"));
        item.setPrice(rs.getDouble("price"));
        item.setDiscount(rs.getDouble("discount"));
        item.setStock(rs.getInt("stock"));
        item.setImageUrl(rs.getString("image_url"));
        return item;
    }
}