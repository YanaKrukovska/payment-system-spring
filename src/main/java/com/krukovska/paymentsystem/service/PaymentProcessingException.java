package com.krukovska.paymentsystem.service;

public class PaymentProcessingException extends RuntimeException {

    public PaymentProcessingException(String message) {
        super(message);
    }
}
