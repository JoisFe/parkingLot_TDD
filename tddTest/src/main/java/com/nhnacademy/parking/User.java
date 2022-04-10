package com.nhnacademy.parking;

import com.nhnacademy.parking.exception.InvalidParkingException;
import com.nhnacademy.parking.exception.LessMoneyThanPaymentException;
import java.time.LocalDateTime;

public class User {
    private String id;
    private Money amount;
    private Car car;
    private boolean paycoUser;
    private ParkingTicket parkingTicket;

    public User(String id, Money amount, Car car, boolean paycoUser,
                ParkingTicket parkingTicket) {
        this.id = id;
        this.amount = amount;
        this.car = car;
        this.paycoUser = paycoUser;
        this.parkingTicket = parkingTicket;
    }

    public Car getCar() {
        return car;
    }

    public boolean isPaycoUser() {
        return paycoUser;
    }

    public Money getAmount() {
        return amount;
    }

    public ParkingTicket getParkingTicket() {
        return parkingTicket;
    }

    boolean parkingToParkingLot(String code, Car car, ParkingLot parkingLot) {
        ParkingSpace parkingSpace = parkingLot.findParkingSpaceByCode(code);
        if (parkingSpace.isAvailable()) {
            parkingSpace.setStartParkDateTime(LocalDateTime.now());
            parkingSpace.closeAvailable();
            parkingSpace.parkingCar(car);
            return true;
        }

        throw new InvalidParkingException("Invalid Parking");
    }

    public void pay(long parkingPayment) {
        if (this.amount.getAmount() < parkingPayment) {
            throw new LessMoneyThanPaymentException("Less Money Than Parking Payment");
        }
        this.amount.payAmount(parkingPayment);
    }
}
