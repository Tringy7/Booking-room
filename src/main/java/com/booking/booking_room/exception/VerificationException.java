package com.booking.booking_room.exception;

public class VerificationException extends RuntimeException {
    public VerificationException(String message) {
        super(message);
    }
    public VerificationException(String message, Throwable cause) {
        super(message, cause);
    }
}
