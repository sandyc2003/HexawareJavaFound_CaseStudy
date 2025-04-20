/*
 * Author : Jananii P
 * Date : 19/04/25
 * Description :  Service interface for handling order-related operations in the 
 *               e-commerce application. It defines methods for managing customers, 
 *               products, cart operations, and order placement.

 */
package com.hexaware.ecommerce.user.service;

import com.hexaware.ecommerce.user.entity.Customer;
import com.hexaware.ecommerce.user.entity.Product;
import java.util.List;
import java.util.Map;

public interface IOrderService {
    boolean createCustomer(Customer customer);
    boolean createProduct(Product product);
    boolean deleteCustomer(int customerId);
    boolean deleteProduct(int productId);
    boolean addToCart(Customer customer, Product product, int quantity);
    boolean removeFromCart(Customer customer, Product product);
    List<Product> viewCart(Customer customer);
    boolean placeOrder(Customer customer, List<Map<Product, Integer>> cartItems, String paymentMode);
    List<Product> getCustomerOrders(int customerId);
}