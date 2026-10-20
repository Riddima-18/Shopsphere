package com.shopsphere.service;

import com.shopsphere.dao.CartDAO;
import com.shopsphere.dao.ProductDAO;
import com.shopsphere.model.CartItem;
import com.shopsphere.model.Product;
import java.sql.SQLException;
import java.util.List;

public class CartService {

    private final CartDAO cartDAO = new CartDAO();
    private final ProductDAO productDAO = new ProductDAO();

    // Adds to the cart, or increases the quantity if the product is already there
    public void add(int userId, int productId, int quantity) throws SQLException {
        if (quantity < 1) {
            throw new IllegalArgumentException("Quantity must be at least 1.");
        }
        Product product = productDAO.findById(productId);
        CartItem existing = cartDAO.findItem(userId, productId);
        int newQuantity = quantity + (existing == null ? 0 : existing.getQuantity());
        checkStock(product, newQuantity);

        if (existing == null) {
            cartDAO.insert(userId, productId, newQuantity);
        } else {
            cartDAO.updateQuantity(userId, productId, newQuantity);
        }
    }

    // A quantity of 0 removes the item
    public void updateQuantity(int userId, int productId, int quantity) throws SQLException {
        if (quantity < 0) {
            throw new IllegalArgumentException("Quantity cannot be negative.");
        }
        if (quantity == 0) {
            cartDAO.delete(userId, productId);
            return;
        }
        Product product = productDAO.findById(productId);
        checkStock(product, quantity);
        cartDAO.updateQuantity(userId, productId, quantity);
    }

    public void remove(int userId, int productId) throws SQLException {
        cartDAO.delete(userId, productId);
    }

    public List<CartItem> getCart(int userId) throws SQLException {
        return cartDAO.findByUser(userId);
    }

    // Totals are calculated here on the server, never from browser input
    public double calculateTotal(List<CartItem> items) {
        double total = 0;
        for (CartItem item : items) {
            total += item.getLineTotal();
        }
        return Math.round(total * 100) / 100.0;
    }

    private void checkStock(Product product, int quantity) {
        if (product == null || !product.isStatus()) {
            throw new IllegalArgumentException("This product is no longer available.");
        }
        if (quantity > product.getStock()) {
            throw new IllegalArgumentException("Only " + product.getStock() + " left in stock.");
        }
    }
}