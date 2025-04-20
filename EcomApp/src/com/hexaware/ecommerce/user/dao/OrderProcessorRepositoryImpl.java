/*
Author: Sandhya C
Date: 18/04/25
Description: Class which implements DAO interface and includes JDBC commands 
*/

package com.hexaware.ecommerce.user.dao;

import java.sql.*;
import java.util.*;

import com.hexaware.ecommerce.user.entity.*;
import com.hexaware.ecommerce.user.exception.CustomerNotFoundException;
import com.hexaware.ecommerce.user.exception.ProductNotFoundException;
import com.hexaware.ecommerce.user.exception.OrderNotFoundException;
import com.hexaware.ecommerce.user.util.DBConnUtil;

public class OrderProcessorRepositoryImpl implements OrderProcessorRepository {

    private Connection conn;

    public OrderProcessorRepositoryImpl() {
        try {
			conn = DBConnUtil.getDBConnection();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    private boolean customerExists(int customerId) throws SQLException {
        String sql = "SELECT 1 FROM customers WHERE customer_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, customerId);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        }
    }

    private boolean productExists(int productId) throws SQLException {
        String sql = "SELECT 1 FROM products WHERE product_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, productId);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        }
    }

    @Override
    public boolean createProduct(Product product) {
        String sql = "INSERT INTO products (product_id, name, price, description, stockQuantity) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, product.getId());
            stmt.setString(2, product.getName());
            stmt.setDouble(3, product.getPrice());
            stmt.setString(4, product.getDescription()); 
            stmt.setInt(5, product.getQuantity()); 
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean createCustomer(Customer customer) {
        String sql = "INSERT INTO customers (customer_id, name, email, password) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, customer.getCustomerId());
            stmt.setString(2, customer.getName());
            stmt.setString(3, customer.getEmail());
            stmt.setString(4, customer.getPassword());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deleteProduct(int productId) {
        String sql = "DELETE FROM products WHERE product_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, productId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deleteCustomer(int customerId) {
        String sql = "DELETE FROM customers WHERE customer_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, customerId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean addToCart(Customer customer, Product product, int quantity) {
        String checkSql = "SELECT quantity FROM cart WHERE customer_id = ? AND product_id = ?";
        String insertSql = "INSERT INTO cart (customer_id, product_id, quantity) VALUES (?, ?, ?)";
        String updateSql = "UPDATE cart SET quantity = quantity + ? WHERE customer_id = ? AND product_id = ?";
        try {
            if (!customerExists(customer.getCustomerId())) {
                throw new CustomerNotFoundException("Customer ID not found: " + customer.getCustomerId());
            }
            if (!productExists(product.getId())) {
                throw new ProductNotFoundException("Product ID not found: " + product.getId());
            }

            try (PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
                checkStmt.setInt(1, customer.getCustomerId());
                checkStmt.setInt(2, product.getId());
                try (ResultSet rs = checkStmt.executeQuery()) {
                    if (rs.next()) {
                        try (PreparedStatement updateStmt = conn.prepareStatement(updateSql)) {
                            updateStmt.setInt(1, quantity);
                            updateStmt.setInt(2, customer.getCustomerId());
                            updateStmt.setInt(3, product.getId());
                            return updateStmt.executeUpdate() > 0;
                        }
                    } else {
                        try (PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {
                            insertStmt.setInt(1, customer.getCustomerId());
                            insertStmt.setInt(2, product.getId());
                            insertStmt.setInt(3, quantity);
                            return insertStmt.executeUpdate() > 0;
                        }
                    }
                }
            }
        } catch (SQLException | CustomerNotFoundException | ProductNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean removeFromCart(Customer customer, Product product) {
        String sql = "DELETE FROM cart WHERE customer_id = ? AND product_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, customer.getCustomerId());
            stmt.setInt(2, product.getId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<Product> getAllFromCart(Customer customer) {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT p.product_id, p.name, p.price, p.stockQuantity FROM cart c JOIN products p ON c.product_id = p.product_id WHERE c.customer_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, customer.getCustomerId());
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Product p = new Product(
                            rs.getInt("product_id"),
                            rs.getString("name"),
                            rs.getDouble("price"),
                            rs.getInt("stockQuantity") // mapped to quantity
                    );
                    products.add(p);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return products;
    }

    @Override
    public boolean placeOrder(Customer customer, List<Map<Product, Integer>> items, String shippingAddress) {
        String orderSql = "INSERT INTO orders (customer_id, order_date, total_price, shipping_address) VALUES (?, NOW(), ?, ?)";
        String itemSql = "INSERT INTO order_items (order_id, product_id, quantity) VALUES (?, ?, ?)";
        try {
            if (!customerExists(customer.getCustomerId())) {
                throw new CustomerNotFoundException("Customer ID not found: " + customer.getCustomerId());
            }

            for (Map<Product, Integer> item : items) {
                for (Product p : item.keySet()) {
                    if (!productExists(p.getId())) {
                        throw new ProductNotFoundException("Product ID not found: " + p.getId());
                    }
                }
            }

            conn.setAutoCommit(false);

            double total = 0.0;
            for (Map<Product, Integer> item : items) {
                for (Product p : item.keySet()) {
                    total += p.getPrice() * item.get(p);
                }
            }

            int orderId;
            try (PreparedStatement orderStmt = conn.prepareStatement(orderSql, Statement.RETURN_GENERATED_KEYS)) {
                orderStmt.setInt(1, customer.getCustomerId());
                orderStmt.setDouble(2, total);
                orderStmt.setString(3, shippingAddress);
                orderStmt.executeUpdate();
                try (ResultSet keys = orderStmt.getGeneratedKeys()) {
                    if (keys.next()) {
                        orderId = keys.getInt(1);
                    } else {
                        conn.rollback();
                        return false;
                    }
                }
            }

            try (PreparedStatement itemStmt = conn.prepareStatement(itemSql)) {
                for (Map<Product, Integer> item : items) {
                    for (Product p : item.keySet()) {
                        itemStmt.setInt(1, orderId);
                        itemStmt.setInt(2, p.getId());
                        itemStmt.setInt(3, item.get(p));
                        itemStmt.addBatch();
                    }
                }
                itemStmt.executeBatch();
            }

            conn.commit();
            return true;
        } catch (SQLException | CustomerNotFoundException | ProductNotFoundException e) {
            try {
                conn.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
            return false;
        } finally {
            try {
                conn.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public List<Map<Product, Integer>> getOrdersByCustomer(int customerId) throws OrderNotFoundException {
        List<Map<Product, Integer>> orders = new ArrayList<>();
        String sql = "SELECT p.product_id, p.name, p.price, p.stockQuantity, oi.quantity " +
                     "FROM orders o " +
                     "JOIN order_items oi ON o.order_id = oi.order_id " +
                     "JOIN products p ON oi.product_id = p.product_id " +
                     "WHERE o.customer_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, customerId);
            try (ResultSet rs = stmt.executeQuery()) {
                boolean hasResults = false;
                while (rs.next()) {
                    hasResults = true;
                    Product p = new Product(
                            rs.getInt("product_id"),
                            rs.getString("name"),
                            rs.getDouble("price"),
                            rs.getInt("stockQuantity")
                    );
                    int quantity = rs.getInt("quantity");
                    Map<Product, Integer> map = new HashMap<>();
                    map.put(p, quantity);
                    orders.add(map);
                }
                if (!hasResults) {
                    throw new OrderNotFoundException("No orders found for customer ID: " + customerId);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orders;
    }
}