package com.nhnacademy.parking;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.nhnacademy.parking.exception.LessMoneyThanPaymentException;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class UserPaymentServiceTest {
    private ParkingSystem parkingSystem;
    private Exit exit;
    private ParkingLot parkingLot;
    private Car car;
    private Car lightCar;
    private ParkingSpace parkingSpace;
    private ParkingSpace parkingSpace2;
    private User user;
    private User user2;
    private Money money;
    private Money money2;

    @BeforeEach
    void setUp() {
        parkingSystem = new ParkingSystem(parkingLot);
        parkingLot = mock(ParkingLot.class);

        car = new Car("1111", CarType.REGULAR_CAR);
        boolean available = false;
        String code = "A-1";

        money = new Money(10_000L);
        user = new User("jo", money, car, false, new ParkingTicket(0, 0));

        when(parkingLot.findUserByCar(car)).thenReturn(user);

        parkingSpace = new ParkingSpace(code, available, car);
        parkingSpace.setStartParkDateTime(LocalDateTime.now());

        lightCar = new Car("1111", CarType.LIGHT_CAR);
        parkingSpace2 = new ParkingSpace(code, available, lightCar);
        parkingSpace2.setStartParkDateTime(LocalDateTime.now());

        money2 = new Money(10_000L);
        user2 = new User("jo", money2, lightCar, false, new ParkingTicket(0, 0));

        when(parkingLot.findUserByCar(lightCar)).thenReturn(user2);

    }

    @DisplayName("주차장에서 나갈때 user가 결제 후 잔액에서 요금만큼 차액이 생기는지 확인 (일반 차, 30분 1초 주차 -> 1,000원 이므로 9,000원이 남아있어야함)")
    @Test
    public void newPaymentPolicyPayTest1() {
        parkingSpace.setEndParkDateTime(parkingSpace.getStartParkDateTime().plusMinutes(30).plusSeconds(1));

        when(parkingLot.findParkingSpaceByNumber(car.getNumber())).thenReturn(parkingSpace);
        exit = new Exit(parkingLot, user); // parkingLotd의 stubbing 지정 이후 exit을 생성해야 (parkingLot을 생성자 인자로 가지기 때문)

        // when
        long parkingPayment = exit.newPaymentPolicyPay(user);
        user.pay(parkingPayment);

        // then
        assertThat(user.getAmount().getAmount()).isEqualTo(9_000L);

        //결제 후 해당 parkingSpace는 이용가능하고 차가 존재하면 안되니(null) 그것 또한 테스트
        assertThat(parkingSpace.isAvailable()).isTrue();
        assertThat(parkingSpace.getCar()).isNull();
    }

    @DisplayName("주차장에서 나갈때 user가 결제 후 잔액에서 요금만큼 차액이 생기는지 확인 (경차, 30분 1초 주차 -> 500원 이므로 9,500원이 남아있어야함)")
    @Test
    public void newPaymentPolicyLightCarPayTest1() {
        parkingSpace2.setEndParkDateTime(parkingSpace2.getStartParkDateTime().plusMinutes(30).plusSeconds(1));

        when(parkingLot.findParkingSpaceByNumber(lightCar.getNumber())).thenReturn(parkingSpace2);
        exit = new Exit(parkingLot, user2); // parkingLotd의 stubbing 지정 이후 exit을 생성해야 (parkingLot을 생성자 인자로 가지기 때문)

        // when
        long parkingPayment = exit.newPaymentPolicyPay(user2);
        user2.pay(parkingPayment);

        // then
        assertThat(user2.getAmount().getAmount()).isEqualTo(9_500L);

        //결제 후 해당 parkingSpace는 이용가능하고 차가 존재하면 안되니(null) 그것 또한 테스트
        assertThat(parkingSpace2.isAvailable()).isTrue();
        assertThat(parkingSpace2.getCar()).isNull();
    }

    @DisplayName("주차장 나갈때 user가 가진돈 보다 주차비가 더 많은 경우 에러 던지기")
    @Test
    public void lessAmountThanPaymentExceptionTest() {
        Money money = new Money(0L);
        User user = new User("jo", money, car, false, new ParkingTicket(0, 0));

        when(parkingLot.findUserByCar(car)).thenReturn(user);

        parkingSpace = new ParkingSpace("A-1", false, car);
        parkingSpace.setStartParkDateTime(LocalDateTime.now());
        parkingSpace.setEndParkDateTime(parkingSpace.getStartParkDateTime().plusMinutes(30).plusSeconds(1));

        when(parkingLot.findParkingSpaceByNumber(car.getNumber())).thenReturn(parkingSpace);
        exit = new Exit(parkingLot, user);

        // when
        long parkingPayment = exit.newPaymentPolicyPay(user2);

        // then
        assertThatThrownBy(() -> user.pay(parkingPayment))
            .isInstanceOf(LessMoneyThanPaymentException.class)
                .hasMessage("Less Money Than Parking Payment");
    }
}
