/*
 * Author : Jananii P
 * Date : 19/04/25
 * Description : This class handles business logic for customer and product management, cart operations,
 *               and order processing in the e-commerce application. 

 */
package com.hexaware.ecommerce.user.service;

import com.hexaware.ecommerce.user.dao.OrderProcessorRepository;
import com.hexaware.ecommerce.user.entity.Customer;
import com.hexaware.ecommerce.user.entity.Product;
import com.hexaware.ecommerce.user.exception.CustomerNotFoundException;
import com.hexaware.ecommerce.user.exception.ProductNotFoundException;
import com.hexaware.ecommerce.user.exception.OrderNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;



public class OrderServiceImpl implements IOrderService {

    private OrderProcessorRepository repository;

    public OrderServiceImpl(OrderProcessorRepository repository) {
        this.repository = repository;
    }

    @Override
    public boolean createCustomer(Customer customer) {
        if (customer == null ||
            customer.getName() == null || customer.getName().trim().isEmpty() ||
            customer.getEmail() == null || !customer.getEmail().matches("^[\\w.-]+@[\\w.-]+\\.\\w+$") ||
            customer.getPassword() == null || customer.getPassword().trim().isEmpty()) { // Only validate name, email, and password
            return false;
        }
        return repository.createCustomer(customer);
    }

    @Override
    public boolean createProduct(Product product) {
        if (product == null ||
            product.getName() == null || product.getName().trim().isEmpty() ||
            product.getPrice() <= 0 ||
            product.getQuantity() <= 0) {
            return false;
        }
        return repository.createProduct(product);
    }

    @Override
    public boolean deleteCustomer(int customerId) {
        if (customerId <= 0) {
            return false;
        }
        return repository.deleteCustomer(customerId);
    }

    @Override
    public boolean deleteProduct(int productId) {
        if (productId <= 0) {
            return false;
        }
        return repository.deleteProduct(productId);
    }

    @Override
    public boolean addToCart(Customer customer, Product product, int quantity) {
        if (customer == null || customer.getCustomerId() <= 0) {
            throw new CustomerNotFoundException("Invalid customer.");
        }
        if (product == null || product.getId() <= 0) {
            throw new ProductNotFoundException("Invalid product.");
        }
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than 0.");
        }
        return repository.addToCart(customer, product, quantity);
    }

    @Override
    public boolean removeFromCart(Customer customer, Product product) {
        if (customer == null || customer.getCustomerId() <= 0) {
            throw new CustomerNotFoundException("Invalid customer.");
        }
        if (product == null || product.getId() <= 0) {
            throw new ProductNotFoundException("Invalid product.");
        }
        return repository.removeFromCart(customer, product);
    }

    @Override
    public List<Product> viewCart(Customer customer) {
        if (customer == null || customer.getCustomerId() <= 0) {
            throw new IllegalArgumentException("Invalid customer.");
        }
        return repository.getAllFromCart(customer);
    }

    @Override
    public boolean placeOrder(Customer customer, List<Map<Product, Integer>> cartItems, String paymentMode) {
        if (customer == null || customer.getCustomerId() <= 0) {
            return false;
        }
        if (cartItems == null || cartItems.isEmpty()) {
            return false;
        }
        if (paymentMode == null || paymentMode.trim().isEmpty()) {
            return false;
        }
        return repository.placeOrder(customer, cartItems, paymentMode);
    }

    @Override
    public List<Product> getCustomerOrders(int customerId) {
        if (customerId <= 0) {
            throw new IllegalArgumentException("Invalid customer ID.");
        }
        try {
            List<Map<Product, Integer>> orderMaps = repository.getOrdersByCustomer(customerId);
            List<Product> products = new ArrayList<>();
            for (Map<Product, Integer> map : orderMaps) {
                products.addAll(map.keySet());
            }
            return products;
        } catch (OrderNotFoundException e) {
            
            e.printStackTrace();
            return new ArrayList<>();  // Return an empty list in case of exception
        }
    }
}