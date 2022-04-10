package com.nhnacademy.parking;

public interface ParkingLot {

    ParkingSpace findParkingSpaceByNumber(String number);

    ParkingSpace findParkingSpaceByCode(String code);

    User findUserByCar(Car car);
}
