/*
Author: Sandhya C
Date: 18/04/25
Description: Includes Variables, Getter Setter methods, Default and Parameterized Constructors
*/
package com.hexaware.ecommerce.user.entity;

import java.util.Date;

public class Orders {
    private int orderId;
    private int customerId;
    private Date orderDate;
    private double totalPrice;
    private String shippingAddress;

    public Orders() {}

    public Orders(int orderId, int customerId, Date orderDate, double totalPrice, String shippingAddress) {
        this.orderId = orderId;
        this.customerId = customerId;
        this.orderDate = orderDate;
        this.totalPrice = totalPrice;
        this.shippingAddress = shippingAddress;
    }

    public int getOrderId() { return orderId; }
    public void setOrderId(int orderId) { this.orderId = orderId; }

    public int getCustomerId() { return customerId; }
    public void setCustomerId(int customerId) { this.customerId = customerId; }

    public Date getOrderDate() { return orderDate; }
    public void setOrderDate(Date orderDate) { this.orderDate = orderDate; }

    public double getTotalPrice() { return totalPrice; }
    public void setTotalPrice(double totalPrice) { this.totalPrice = totalPrice; }

    public String getShippingAddress() { return shippingAddress; }
    public void setShippingAddress(String shippingAddress) { this.shippingAddress = shippingAddress; }
}