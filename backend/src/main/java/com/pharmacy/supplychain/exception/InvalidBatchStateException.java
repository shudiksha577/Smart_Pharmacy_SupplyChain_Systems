package com.pharmacy.supplychain.exception;

public class InvalidBatchStateException extends RuntimeException {
    public InvalidBatchStateException(String message) {
        super(message);
    }
}
