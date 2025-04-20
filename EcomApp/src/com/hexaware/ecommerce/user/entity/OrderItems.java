/*
Author: Sandhya C
Date: 18/04/25
Description: Includes Variables, Getter Setter methods, Default and Parameterized Constructors
*/
package com.hexaware.ecommerce.user.entity;

public class OrderItems {
    private int orderItemId;
    private int orderId;
    private int productId;
    private int quantity;

    public OrderItems() {}

    public OrderItems(int orderItemId, int orderId, int productId, int quantity) {
        this.orderItemId = orderItemId;
        this.orderId = orderId;
        this.productId = productId;
        this.quantity = quantity;
    }

    public int getOrderItemId() { return orderItemId; }
    public void setOrderItemId(int orderItemId) { this.orderItemId = orderItemId; }

    public int getOrderId() { return orderId; }
    public void setOrderId(int orderId) { this.orderId = orderId; }

    public int getProductId() { return productId; }
    public void setProductId(int productId) { this.productId = productId; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
}