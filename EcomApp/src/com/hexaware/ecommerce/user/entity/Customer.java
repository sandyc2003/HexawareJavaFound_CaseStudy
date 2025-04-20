/*
Author: Sandhya C
Date: 18/04/25
Description: Includes Variables, Getter Setter methods, Default and Parameterized Constructors
*/

package com.hexaware.ecommerce.user.entity;

public class Customer {
    private int customerId;
    private String name;
    private String email;
    private String password;

    
    public Customer() {
    }

    // Constructor with all fields
    public Customer(int customerId, String name, String email, String password) {
        this.customerId = customerId;
        this.name = name;
        this.email = email;
        this.password = password;
    }

    // Getters
    public int getCustomerId() {
        return customerId;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    // Setters
    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

	public Customer(String name, String email, String password) {
		super();
		this.name = name;
		this.email = email;
		this.password = password;
	}
}