package com.example.demo.Exception;

public class CustomException extends RuntimeException{
    private final int statusCode;


    public CustomException(String message, int statusCode) {
        super(message);
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return this.statusCode;
    }
}
