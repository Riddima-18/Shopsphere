package com.shopsphere.dao;

import com.shopsphere.model.Product;
import com.shopsphere.util.DBUtil;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductDAO {

    // ---------- Customer queries: active products only ----------

    public List<Product> findAll() throws SQLException {
        return list("SELECT * FROM products WHERE status = TRUE ORDER BY product_id");
    }

    public List<Product> search(String keyword) throws SQLException {
        return list("SELECT * FROM products WHERE status = TRUE AND name LIKE ? ORDER BY name",
                "%" + keyword + "%");
    }

    // Returns the product whether or not it is active; the caller checks the status
    public Product findById(int id) throws SQLException {
        List<Product> found = list("SELECT * FROM products WHERE product_id = ?", id);
        return found.isEmpty() ? null : found.get(0);
    }

    // ---------- Admin queries: all products ----------

    public List<Product> findAllForAdmin() throws SQLException {
        return list("SELECT * FROM products ORDER BY product_id");
    }

    public List<Product> findLowStock(int threshold) throws SQLException {
        return list("SELECT * FROM products WHERE stock <= ? ORDER BY stock", threshold);
    }

    public void insert(Product p) throws SQLException {
        String sql = "INSERT INTO products (category_id, name, brand, description, price, discount, " +
                     "stock, image_url, status) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, p.getCategoryId());
            ps.setString(2, p.getName());
            ps.setString(3, p.getBrand());
            ps.setString(4, p.getDescription());
            ps.setDouble(5, p.getPrice());
            ps.setDouble(6, p.getDiscount());
            ps.setInt(7, p.getStock());
            ps.setString(8, p.getImageUrl());
            ps.setBoolean(9, p.isStatus());
            ps.executeUpdate();
        }
    }

    public void update(Product p) throws SQLException {
        String sql = "UPDATE products SET category_id = ?, name = ?, brand = ?, description = ?, " +
                     "price = ?, discount = ?, stock = ?, image_url = ?, status = ? WHERE product_id = ?";
        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, p.getCategoryId());
            ps.setString(2, p.getName());
            ps.setString(3, p.getBrand());
            ps.setString(4, p.getDescription());
            ps.setDouble(5, p.getPrice());
            ps.setDouble(6, p.getDiscount());
            ps.setInt(7, p.getStock());
            ps.setString(8, p.getImageUrl());
            ps.setBoolean(9, p.isStatus());
            ps.setInt(10, p.getProductId());
            ps.executeUpdate();
        }
    }

    public void setStatus(int productId, boolean status) throws SQLException {
        String sql = "UPDATE products SET status = ? WHERE product_id = ?";
        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setBoolean(1, status);
            ps.setInt(2, productId);
            ps.executeUpdate();
        }
    }

    // ---------- Helpers ----------

    // Runs a query with any number of parameters and maps every row to a Product
    private List<Product> list(String sql, Object... params) throws SQLException {
        List<Product> items = new ArrayList<>();
        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            for (int i = 0; i < params.length; i++) {
                ps.setObject(i + 1, params[i]);
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    items.add(map(rs));
                }
            }
        }
        return items;
    }

    private Product map(ResultSet rs) throws SQLException {
        Product p = new Product();
        p.setProductId(rs.getInt("product_id"));
        p.setCategoryId(rs.getInt("category_id"));
        p.setName(rs.getString("name"));
        p.setBrand(rs.getString("brand"));
        p.setDescription(rs.getString("description"));
        p.setPrice(rs.getDouble("price"));
        p.setDiscount(rs.getDouble("discount"));
        p.setStock(rs.getInt("stock"));
        p.setImageUrl(rs.getString("image_url"));
        p.setStatus(rs.getBoolean("status"));
        return p;
    }
}