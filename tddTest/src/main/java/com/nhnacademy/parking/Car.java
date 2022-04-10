package com.nhnacademy.parking;

import java.util.Objects;

public class Car {
    String number;
    CarType carType;

    public Car(String number, CarType carType) {
        this.number = number;
        this.carType = carType;
    }

    public String getNumber() {
        return number;
    }

    public CarType getCarType() {
        return carType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Car car = (Car) o;
        return Objects.equals(number, car.number);
    }

    @Override
    public int hashCode() {
        return Objects.hash(number);
    }
}
