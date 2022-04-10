package com.nhnacademy.parking;

public class Money {
    private long amount;

    public Money(long amount) {
        this.amount = amount;
    }

    public long getAmount() {
        return amount;
    }

    public void payAmount(long parkingPayment) {
        this.amount -= parkingPayment;
    }
}
