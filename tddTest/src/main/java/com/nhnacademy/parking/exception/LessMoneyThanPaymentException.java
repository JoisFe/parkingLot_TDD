package com.nhnacademy.parking.exception;

public class LessMoneyThanPaymentException extends IllegalArgumentException {
    public LessMoneyThanPaymentException(String s) {
        super(s);
    }
}
