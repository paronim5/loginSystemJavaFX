package org.example.loginsystem;

/**
 * custom exception if user is not found in database
 */
public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String message) {
        super(message);
    }
}
