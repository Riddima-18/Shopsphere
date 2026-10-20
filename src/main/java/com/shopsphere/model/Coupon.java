package com.shopsphere.model;

import java.time.LocalDate;

public class Coupon {
    private int couponId;
    private String code;
    private String discountType;     // PERCENT or FLAT
    private double discountValue;
    private double minimumOrder;
    private Double maximumDiscount;  // null means no cap
    private LocalDate expiryDate;    // null means no expiry
    private boolean status;

    public int getCouponId() { return couponId; }
    public void setCouponId(int couponId) { this.couponId = couponId; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public String getDiscountType() { return discountType; }
    public void setDiscountType(String discountType) { this.discountType = discountType; }

    public double getDiscountValue() { return discountValue; }
    public void setDiscountValue(double discountValue) { this.discountValue = discountValue; }

    public double getMinimumOrder() { return minimumOrder; }
    public void setMinimumOrder(double minimumOrder) { this.minimumOrder = minimumOrder; }

    public Double getMaximumDiscount() { return maximumDiscount; }
    public void setMaximumDiscount(Double maximumDiscount) { this.maximumDiscount = maximumDiscount; }

    public LocalDate getExpiryDate() { return expiryDate; }
    public void setExpiryDate(LocalDate expiryDate) { this.expiryDate = expiryDate; }

    public boolean isStatus() { return status; }
    public void setStatus(boolean status) { this.status = status; }
}