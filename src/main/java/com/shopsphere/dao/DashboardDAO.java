package com.shopsphere.dao;

import com.shopsphere.util.DBUtil;
import java.sql.*;

public class DashboardDAO {

    public int countProducts() throws SQLException {
        return count("SELECT COUNT(*) FROM products");
    }

    public int countOrders() throws SQLException {
        return count("SELECT COUNT(*) FROM orders");
    }

    // Revenue from payments that were actually marked PAID (card orders)
    public double paidRevenue() throws SQLException {
        String sql = "SELECT COALESCE(SUM(amount), 0) FROM payments WHERE payment_status = 'PAID'";
        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            rs.next();
            return rs.getDouble(1);
        }
    }

    private int count(String sql) throws SQLException {
        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            rs.next();
            return rs.getInt(1);
        }
    }
}