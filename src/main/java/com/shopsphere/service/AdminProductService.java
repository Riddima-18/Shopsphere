package com.shopsphere.service;

import com.shopsphere.dao.ProductDAO;
import com.shopsphere.model.Product;
import java.sql.SQLException;

public class AdminProductService {

    private final ProductDAO productDAO = new ProductDAO();

    // Inserts a new product, or updates one if it already has an ID
    public void save(Product p) throws SQLException {
        p.setName(clean(p.getName()));
        p.setBrand(clean(p.getBrand()));
        p.setDescription(clean(p.getDescription()));
        p.setImageUrl(clean(p.getImageUrl()));

        if (p.getName().isEmpty()) {
            throw new IllegalArgumentException("Product name is required.");
        }
        if (p.getCategoryId() <= 0) {
            throw new IllegalArgumentException("Choose a category.");
        }
        if (p.getPrice() <= 0) {
            throw new IllegalArgumentException("Price must be greater than 0.");
        }
        if (p.getDiscount() < 0 || p.getDiscount() > 100) {
            throw new IllegalArgumentException("Discount must be between 0 and 100.");
        }
        if (p.getStock() < 0) {
            throw new IllegalArgumentException("Stock cannot be negative.");
        }

        if (p.getProductId() > 0) {
            productDAO.update(p);
        } else {
            productDAO.insert(p);
        }
    }

    // Enables a disabled product, or disables an active one
    public void toggleStatus(int productId) throws SQLException {
        Product product = productDAO.findById(productId);
        if (product == null) {
            throw new IllegalArgumentException("Product not found.");
        }
        productDAO.setStatus(productId, !product.isStatus());
    }

    private String clean(String value) {
        return value == null ? "" : value.trim();
    }
}