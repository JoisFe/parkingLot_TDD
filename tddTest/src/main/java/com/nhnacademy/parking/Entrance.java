package com.nhnacademy.parking;

import com.nhnacademy.parking.exception.TruckInvalidParkingExcetpion;

public class Entrance implements Runnable {
    private ParkingLot parkingLot;

    public Entrance(ParkingLot parkingLot) {
        this.parkingLot = parkingLot;
    }

    public String scan(Car car) {
        if (car.getCarType().equals(CarType.TRUCK)) {
            throw new TruckInvalidParkingExcetpion("Invalid Truck Parking");
        }
        return car.getNumber();
    }

    @Override
    public void run() {
        try {
            Thread.sleep(1000);
            System.out.println("entrance 실행중 (1초 지연 시킴)");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
