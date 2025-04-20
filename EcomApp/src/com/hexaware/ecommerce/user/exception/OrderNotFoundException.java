/*
 * Author : Jananii P
 * Date : 18/04/25
 * Description :  * Description : Custom checked exception class that is thrown when an order 
 *               is not found in the e-commerce application.

 */
package com.hexaware.ecommerce.user.exception;

public class OrderNotFoundException extends Exception {
    public OrderNotFoundException(String message) {
        super(message);
    }
}