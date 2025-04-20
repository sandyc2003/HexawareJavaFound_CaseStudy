/*
 * Author : Jananii P
 * Date : 18/04/25
 * Description : Custom unchecked exception class that is thrown when a product 
 *               is not found in the e-commerce application.

 * 
 */
package com.hexaware.ecommerce.user.exception;

public class ProductNotFoundException extends RuntimeException {
    public ProductNotFoundException(String message) {
        super(message);
    }
}