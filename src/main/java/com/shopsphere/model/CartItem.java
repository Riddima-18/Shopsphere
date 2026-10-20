package com.shopsphere.model;

public class CartItem {
    private int productId;
    private String name;
    private double price;
    private double discount;
    private int stock;
    private String imageUrl;
    private int quantity;

    public int getProductId() { return productId; }
    public void setProductId(int productId) { this.productId = productId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public double getDiscount() { return discount; }
    public void setDiscount(double discount) { this.discount = discount; }

    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public double getFinalPrice() {
        return price * (1 - discount / 100);
    }

    // Price x quantity, rounded to 2 decimals
    public double getLineTotal() {
        return Math.round(getFinalPrice() * quantity * 100) / 100.0;
    }
}