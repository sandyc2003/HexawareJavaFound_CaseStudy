/*
Author: Sandhya C
Date: 18/04/25
Description: Interface that handles methods to be implemented with SQL queries 
*/

package com.hexaware.ecommerce.user.dao;

import java.util.List;
import java.util.Map;

import com.hexaware.ecommerce.user.entity.Customer;
import com.hexaware.ecommerce.user.entity.Product;
import com.hexaware.ecommerce.user.exception.OrderNotFoundException; // ✅ Add this import

public interface OrderProcessorRepository {
    boolean createProduct(Product product);
    boolean createCustomer(Customer customer);
    boolean deleteProduct(int productId);
    boolean deleteCustomer(int customerId);
    boolean addToCart(Customer customer, Product product, int quantity);
    boolean removeFromCart(Customer customer, Product product);
    List<Product> getAllFromCart(Customer customer);
    boolean placeOrder(Customer customer, List<Map<Product, Integer>> orderDetails, String shippingAddress);
    List<Map<Product, Integer>> getOrdersByCustomer(int customerId) throws OrderNotFoundException;
}