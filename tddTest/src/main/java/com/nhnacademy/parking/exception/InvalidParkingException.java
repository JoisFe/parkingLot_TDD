package com.nhnacademy.parking.exception;

public class InvalidParkingException extends IllegalArgumentException {
    public InvalidParkingException(String s) {
        super(s);
    }
}
