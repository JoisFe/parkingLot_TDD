package com.nhnacademy.parking;

import java.time.LocalDateTime;

public class ParkingSpace {
    String code;
    boolean available;
    Car car;
    LocalDateTime startParkDateTime;
    LocalDateTime endParkDateTime;

    public ParkingSpace(String code, boolean available, Car car) {
        this.code = code;
        this.available = available;
        this.car = car;
    }

    public boolean isAvailable() {
        return available;
    }

    public Car getCar() {
        return car;
    }

    public LocalDateTime getStartParkDateTime() {
        return startParkDateTime;
    }

    public LocalDateTime getEndParkDateTime() {
        return endParkDateTime;
    }

    public void setStartParkDateTime(LocalDateTime startParkDateTime) {
        this.startParkDateTime = startParkDateTime;
    }

    public void setEndParkDateTime(LocalDateTime endParkDateTime) {
        this.endParkDateTime = endParkDateTime;
    }

    public void closeAvailable() {
        this.available = false;
    }

    public void openAvailable() {
         this.available = true;
    }

    public void parkingCar(Car car) {
        this.car = car;
    }

    public void exitCar() {
        this.car = null;
    }
}
