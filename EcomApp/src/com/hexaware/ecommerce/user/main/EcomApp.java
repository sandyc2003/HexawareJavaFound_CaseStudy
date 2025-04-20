/* Author names: Sandhya C, Jananii P
 Date: 20/04/25
 Descrption: Main class in which menu-driven structure along with switch case is used to call the methods 
 */


package com.hexaware.ecommerce.user.main;

import com.hexaware.ecommerce.user.dao.OrderProcessorRepositoryImpl;
import com.hexaware.ecommerce.user.entity.Customer;
import com.hexaware.ecommerce.user.entity.Product;
import com.hexaware.ecommerce.user.service.OrderServiceImpl;

import java.util.*;

public class EcomApp {

    private static final Scanner sc = new Scanner(System.in);
    private static final OrderServiceImpl service = new OrderServiceImpl(new OrderProcessorRepositoryImpl());

    public static void main(String[] args) {
        while (true) {
            displayMenu();
            int choice = sc.nextInt();
            sc.nextLine(); 

            switch (choice) {
                case 1:
                    registerCustomer();
                    break;

                case 2:
                    addProduct();
                    break;

                case 3:
                    deleteCustomer();
                    break;

                case 4:
                    deleteProduct();
                    break;

                case 5:
                    placeOrder();
                    break;

                case 6:
                    exitApp();
                    break;

                default:
                    System.out.println("❌ Invalid option. Please try again.");
                    break;
            }
        }
    }

    private static void displayMenu() {
        System.out.println("\n1. Register Customer");
        System.out.println("2. Add Product");
        System.out.println("3. Delete Customer");
        System.out.println("4. Delete Product");
        System.out.println("5. Place Order");
        System.out.println("6. Exit");
        System.out.print("Enter your choice: ");
    }

    private static void registerCustomer() {
        System.out.print("Enter Customer Name: ");
        String name = sc.nextLine();

        System.out.print("Enter Email: ");
        String email = sc.nextLine();

        System.out.print("Enter Password: ");
        String password = sc.nextLine();

        Customer newCustomer = new Customer(0, name, email, password);
        if (service.createCustomer(newCustomer)) {
            System.out.println("✅ Customer created successfully.");
        } else {
            System.out.println("❌ Invalid customer data. Please check inputs.");
        }
    }

    private static void addProduct() {
        System.out.print("Enter Product Name: ");
        String pname = sc.nextLine();

        System.out.print("Enter Price: ");
        double price = sc.nextDouble();
        sc.nextLine(); 

        System.out.print("Enter Description: ");
        String description = sc.nextLine();

        System.out.print("Enter Quantity: ");
        int qty = sc.nextInt();
        sc.nextLine(); 

        Product product = new Product(0, pname, price, description, qty);
        if (service.createProduct(product)) {
            System.out.println("✅ Product created successfully.");
        } else {
            System.out.println("❌ Invalid product details.");
        }
    }

    private static void deleteCustomer() {
        System.out.print("Enter Customer ID to delete: ");
        int custId = sc.nextInt();
        sc.nextLine();

        if (service.deleteCustomer(custId)) {
            System.out.println("✅ Customer deleted.");
        } else {
            System.out.println("❌ Invalid Customer ID.");
        }
    }

    private static void deleteProduct() {
        System.out.print("Enter Product ID to delete: ");
        int prodId = sc.nextInt();
        sc.nextLine();

        if (service.deleteProduct(prodId)) {
            System.out.println("✅ Product deleted.");
        } else {
            System.out.println("❌ Invalid Product ID.");
        }
    }

    private static void placeOrder() {
        System.out.print("Enter Customer ID: ");
        int orderCustId = sc.nextInt();
        sc.nextLine();

        System.out.print("Enter Shipping Address: ");
        String shippingAddress = sc.nextLine();

        List<Map<Product, Integer>> cartItems = new ArrayList<>();
        Map<Product, Integer> cartItem1 = new HashMap<>();
        cartItem1.put(new Product(1, "Laptop", 1200.0, "Gaming Laptop", 5), 2); // Hardcoded example
        cartItems.add(cartItem1);

        Customer orderCustomer = new Customer();
        orderCustomer.setCustomerId(orderCustId);

        if (service.placeOrder(orderCustomer, cartItems, shippingAddress)) {
            System.out.println("✅ Order placed successfully.");
        } else {
            System.out.println("❌ Failed to place order. Check input or cart.");
        }
    }

    private static void exitApp() {
        System.out.println(" Exiting...");
        System.exit(0);
    }
}
