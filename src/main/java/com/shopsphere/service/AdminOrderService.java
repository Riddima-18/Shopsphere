package com.shopsphere.service;

import com.shopsphere.dao.AdminOrderDAO;
import com.shopsphere.model.Order;
import com.shopsphere.util.DBUtil;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class AdminOrderService {

    private final AdminOrderDAO orderDAO = new AdminOrderDAO();

    public List<Order> listOrders() throws SQLException {
        return orderDAO.findAll();
    }

    // Allowed moves: PLACED -> SHIPPED, PLACED -> CANCELLED, SHIPPED -> DELIVERED
    public void updateStatus(long orderId, String newStatus) throws SQLException {
        if (!isKnownStatus(newStatus)) {
            throw new IllegalArgumentException("Unknown order status.");
        }

        try (Connection con = DBUtil.getConnection()) {
            con.setAutoCommit(false);
            try {
                String current = orderDAO.lockStatus(con, orderId);
                if (current == null) {
                    throw new IllegalArgumentException("Order not found.");
                }
                if (!isAllowed(current, newStatus)) {
                    throw new IllegalArgumentException(
                            "An order that is " + current + " cannot be changed to " + newStatus + ".");
                }
                if ("CANCELLED".equals(newStatus)) {
                    orderDAO.restoreStock(con, orderId);
                }
                orderDAO.setStatus(con, orderId, newStatus);
                con.commit();
            } catch (SQLException | RuntimeException e) {
                con.rollback();   // undo the status change and stock change together
                throw e;
            }
        }
    }

    private boolean isKnownStatus(String status) {
        return "PLACED".equals(status) || "SHIPPED".equals(status)
                || "DELIVERED".equals(status) || "CANCELLED".equals(status);
    }

    private boolean isAllowed(String from, String to) {
        return switch (from) {
            case "PLACED" -> to.equals("SHIPPED") || to.equals("CANCELLED");
            case "SHIPPED" -> to.equals("DELIVERED");
            default -> false;   // DELIVERED and CANCELLED are final
        };
    }
}