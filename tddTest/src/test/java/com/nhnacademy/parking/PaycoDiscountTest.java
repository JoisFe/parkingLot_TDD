package com.nhnacademy.parking;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class PaycoDiscountTest {
    private ParkingSystem parkingSystem;
    private Exit exit;
    private ParkingLot parkingLot;
    private Car car;
    private Car lightCar;
    private ParkingSpace parkingSpace;
    private ParkingSpace parkingSpace2;

    @BeforeEach
    void setUp() {
        parkingSystem = new ParkingSystem(parkingLot);
        parkingLot = mock(ParkingLot.class);

        car = new Car("1111", CarType.REGULAR_CAR);
        boolean available = false;
        String code = "A-1";

        parkingSpace = new ParkingSpace(code, available, car);
        parkingSpace.setStartParkDateTime(LocalDateTime.now());

        lightCar = new Car("1111", CarType.LIGHT_CAR);
        parkingSpace2 = new ParkingSpace(code, available, lightCar);
        parkingSpace2.setStartParkDateTime(LocalDateTime.now());
    }

    @DisplayName("payco 회원인 user가 결제시 10% 할인 요금 (일반차 30분 1초 주차 할인 후 -> 900원")
    @Test
    public void parKingPaycoUserPaymentPayTest() {
        boolean paycoUser = true;
        User user = new User("jo", null, car, paycoUser, new ParkingTicket(0, 0));
        exit = new Exit(parkingLot, user);

        when(parkingLot.findUserByCar(car)).thenReturn(user);

        parkingSpace.setEndParkDateTime(parkingSpace.getStartParkDateTime().plusMinutes(30).plusSeconds(1));

        when(parkingLot.findParkingSpaceByNumber(car.getNumber())).thenReturn(parkingSpace);

        // when
        Car exitCar = new Car("1111", CarType.REGULAR_CAR);

        // then
        assertThat(exit.newPaymentPolicyPay(user)).isEqualTo(900L);

        //결제 후 해당 parkingSpace는 이용가능하고 차가 존재하면 안되니(null) 그것 또한 테스트
        assertThat(parkingSpace.isAvailable()).isTrue();
        assertThat(parkingSpace.getCar()).isNull();
    }

    @DisplayName("payco 회원인 user가 결제시 10% 할인 요금 (경차 30분 1초 주차 할인 후 -> 450원")
    @Test
    public void parKingPaycoUserLightCarPaymentPayTest() {
        boolean paycoUser = true;
        User user = new User("jo", null, lightCar, paycoUser, new ParkingTicket(0, 0));
        exit = new Exit(parkingLot, user);

        when(parkingLot.findUserByCar(lightCar)).thenReturn(user);

        parkingSpace2.setEndParkDateTime(parkingSpace2.getStartParkDateTime().plusMinutes(30).plusSeconds(1));

        when(parkingLot.findParkingSpaceByNumber(lightCar.getNumber())).thenReturn(parkingSpace2);

        // when
        Car exitCar = new Car("1111", CarType.LIGHT_CAR);

        // then
        assertThat(exit.newPaymentPolicyPay(user)).isEqualTo(450L);

        //결제 후 해당 parkingSpace는 이용가능하고 차가 존재하면 안되니(null) 그것 또한 테스트
        assertThat(parkingSpace2.isAvailable()).isTrue();
        assertThat(parkingSpace2.getCar()).isNull();
    }
}
