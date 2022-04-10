package com.nhnacademy.parking;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;

import com.nhnacademy.parking.exception.TruckInvalidParkingExcetpion;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class TruckParkInTest {
    ParkingSystem parkingSystem;
    ParkingLot parkingLot;

    @BeforeEach
    void setUp() {
        parkingSystem = new ParkingSystem(parkingLot);
        parkingLot = mock(ParkingLot.class);
    }

    @DisplayName("주차장 입구에서 차 번호 스캔하면서 트럭일 시 주차 불가능하다는 에러 테스트")
    @Test
    void truckInvalidParkingExcetpionTest() {
        Entrance entrance = new Entrance(parkingLot);

        //when
        String number = "8282";
        Car truck = new Car(number, CarType.TRUCK);

        //then
        assertThatThrownBy(() -> entrance.scan(truck))
            .isInstanceOf(TruckInvalidParkingExcetpion.class)
            .hasMessage("Invalid Truck Parking");
    }

}
