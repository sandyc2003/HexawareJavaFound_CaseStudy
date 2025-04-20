/*
 * Author :Jananii P
 * Date : 18/04/25
 * Description : Custom exception class that is thrown when a customer is not found in the e-commerce application.
 * 
 */
package com.hexaware.ecommerce.user.exception;

public class CustomerNotFoundException extends RuntimeException {
    public CustomerNotFoundException(String message) {
        super(message);
    }
}