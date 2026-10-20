package com.shopsphere.model;

public class OrderItem {
    private int productId;
    private String name;
    private int quantity;
    private double price;

    public int getProductId() { return productId; }
    public void setProductId(int productId) { this.productId = productId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    // Unit price that was charged when the order was placed
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public double getLineTotal() {
        return Math.round(price * quantity * 100) / 100.0;
    }
}