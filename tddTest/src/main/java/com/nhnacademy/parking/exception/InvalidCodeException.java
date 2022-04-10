package com.nhnacademy.parking.exception;

public class InvalidCodeException extends IllegalArgumentException {
    public InvalidCodeException(String s) {
        super(s);
    }
}
