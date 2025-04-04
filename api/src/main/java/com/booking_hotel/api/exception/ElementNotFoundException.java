package com.booking_hotel.api.exception;


public class ElementNotFoundException extends RuntimeException {
    public ElementNotFoundException (String message) {
        super(message);
    }
}
