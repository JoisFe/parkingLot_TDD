package com.nhnacademy.parking;

public class ParkingTicket {
    private int oneHourParkingTicket;
    private int twoHourParkingTicket;

    public ParkingTicket(int oneHourParkingTicket, int twoHourParkingTicket) {
        this.oneHourParkingTicket = oneHourParkingTicket;
        this.twoHourParkingTicket = twoHourParkingTicket;
    }

    public int getOneHourParkingTicket() {
        return oneHourParkingTicket;
    }

    public int getTwoHourParkingTicket() {
        return twoHourParkingTicket;
    }

    public void useOneHourParkingTicket() {
        --this.oneHourParkingTicket;
    }

    public void useTwoHourParkingTicket() {
        --this.twoHourParkingTicket;
    }
}
