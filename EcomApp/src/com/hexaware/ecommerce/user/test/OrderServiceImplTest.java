/* Author names: Sandhya C, Jananii P
 Date: 20/04/25
 Descrption: JUnit Test class which tests the methods declared in OrderServiceImpl implementation class 
 */

package com.hexaware.ecommerce.user.test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

import com.hexaware.ecommerce.user.dao.OrderProcessorRepositoryImpl;
import com.hexaware.ecommerce.user.entity.Customer;
import com.hexaware.ecommerce.user.entity.Product;
import com.hexaware.ecommerce.user.exception.OrderNotFoundException;

class OrderServiceImplTest {
	OrderProcessorRepositoryImpl repo = new OrderProcessorRepositoryImpl();

	

	@Test
	void testCreateCustomer() {
		Customer customer= new Customer("Sam", "sam@email.com", "sam123");
        boolean result = repo.createCustomer(customer);
        assertTrue(result);
		
	}

	@Test
	void testCreateProduct() {
		  Product product= new Product("CD",100.00,"good quality", 10);
		  boolean result = repo.createProduct(product);
	        assertTrue(result);
	}

	@Test
	void testDeleteCustomer() {
		boolean result=repo.deleteCustomer(5);
		assertTrue(result);
	}

	@Test
	void testDeleteProduct() {
		boolean result=repo.deleteProduct(5);
		assertTrue(result);
	}
	@Test
	void testAddToCart() {
		Customer customer = new Customer(1, "Alice", "alice@mail.com", "password123");
        Product product = new Product(1, "Laptop", 1200.0, "Gaming Laptop", 5);
        boolean result = repo.addToCart(customer, product, 1);
        assertTrue(result);
	}

	@Test
	void testRemoveFromCart() {
		 Customer customer = new Customer(1, "Alice", "alice@mail.com", "password123");
	        Product product = new Product(1, "Laptop", 1200.0, "Gaming Laptop", 5);
	        boolean result = repo.removeFromCart(customer, product);
	        assertTrue(result);
	}

	@Test
	void testViewCart() {
		Customer customer = new Customer(1, "Alice", "alice@mail.com", "password123");
        List<Product> products = repo.getAllFromCart(customer);
        assertNotNull(products);
	}

	
		

	@Test
	void testGetCustomerOrders() {
		try {
			repo.getOrdersByCustomer(3);
		} catch (OrderNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
