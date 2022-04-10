package com.nhnacademy.parking;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ParkingTicketTest {
    private ParkingSystem parkingSystem;
    private Exit exit;
    private ParkingLot parkingLot;
    private Car car;
    private Car lightCar;
    private ParkingSpace parkingSpace;
    private User user;
    private Money money;

    @BeforeEach
    void setUp() {
        parkingSystem = new ParkingSystem(parkingLot);
        parkingLot = mock(ParkingLot.class);

        car = new Car("1111", CarType.REGULAR_CAR);

        lightCar = new Car("1111", CarType.LIGHT_CAR);


    }

    @DisplayName("1일 주차권 사용한 경우 (일반 차, 2시간 주차 -> 4,000원 인데 1시간 주차권 썻으니 1시간 주차 -> 1,000원 -> user는 잔액 9,000)")
    @Test
    public void newPaymentPolicyPayTest1() {
        money = new Money(10_000L);
        // 1시간 주차권 1개 있음
        ParkingTicket parkingTicket = new ParkingTicket(1, 0);
        user = new User("jo", money, car, false, parkingTicket);

        when(parkingLot.findUserByCar(car)).thenReturn(user);

        parkingSpace = new ParkingSpace("A-1", false, car);
        parkingSpace.setStartParkDateTime(LocalDateTime.now());
        parkingSpace.setEndParkDateTime(parkingSpace.getStartParkDateTime().plusHours(2));

        when(parkingLot.findParkingSpaceByNumber(car.getNumber())).thenReturn(parkingSpace);
        exit = new Exit(parkingLot, user); // parkingLotd의 stubbing 지정 이후 exit을 생성해야 (parkingLot을 생성자 인자로 가지기 때문)

        // when
        long parkingPayment = exit.newPaymentPolicyPay(user);
        user.pay(parkingPayment);

        //then
        // user 9_000 잔액 남아야
        assertThat(user.getAmount().getAmount()).isEqualTo(9_000L);

        // 1시간 주차권 1개에서 1개 썻으니 0개 되어야함
        assertThat(user.getParkingTicket().getOneHourParkingTicket()).isEqualTo(0);
    }


    @DisplayName("2일 주차권 사용한 경우 (일반 차, 4시간 주차 -> 10,000원 인데 2시간 주차권 썻으니 2시간 주차 -> 4,000원 -> user는 잔액 6,000)")
    @Test
    public void newPaymentPolicyPayTest2() {
        money = new Money(10_000L);
        // 2시간 주차권 1개, 1시간 주차권 1개 있음
        ParkingTicket parkingTicket = new ParkingTicket(0, 1);
        user = new User("jo", money, car, false, parkingTicket);

        when(parkingLot.findUserByCar(car)).thenReturn(user);

        parkingSpace = new ParkingSpace("A-1", false, car);
        parkingSpace.setStartParkDateTime(LocalDateTime.now());
        parkingSpace.setEndParkDateTime(parkingSpace.getStartParkDateTime().plusHours(4));

        when(parkingLot.findParkingSpaceByNumber(car.getNumber())).thenReturn(parkingSpace);
        exit = new Exit(parkingLot, user); // parkingLotd의 stubbing 지정 이후 exit을 생성해야 (parkingLot을 생성자 인자로 가지기 때문)

        // when
        long parkingPayment = exit.newPaymentPolicyPay(user);
        user.pay(parkingPayment);

        //then
        // user 6_000 잔액 남아야
        assertThat(user.getAmount().getAmount()).isEqualTo(6_000L);

        // 2시간 주차권 1개에서 1개 썻으니 0개 되어야함
        assertThat(user.getParkingTicket().getTwoHourParkingTicket()).isEqualTo(0);
    }

    @DisplayName("1일, 2일 주차권 모두 있는 (일반 차, 1시간 50분 주차 -> 2시간 미만이니 1시간 주차권 사용 : 50분 주차 -> 1,000원 -> user는 잔액 9,000)")
    @Test
    public void newPaymentPolicyPayTest3() {
        money = new Money(10_000L);
        // 2시간 주차권 1개, 1시간 주차권 1개 있음
        ParkingTicket parkingTicket = new ParkingTicket(1, 1);
        user = new User("jo", money, car, false, parkingTicket);

        when(parkingLot.findUserByCar(car)).thenReturn(user);

        parkingSpace = new ParkingSpace("A-1", false, car);
        parkingSpace.setStartParkDateTime(LocalDateTime.now());
        parkingSpace.setEndParkDateTime(parkingSpace.getStartParkDateTime().plusHours(1).plusMinutes(50));

        when(parkingLot.findParkingSpaceByNumber(car.getNumber())).thenReturn(parkingSpace);
        exit = new Exit(parkingLot, user); // parkingLotd의 stubbing 지정 이후 exit을 생성해야 (parkingLot을 생성자 인자로 가지기 때문)

        // when
        long parkingPayment = exit.newPaymentPolicyPay(user);
        user.pay(parkingPayment);

        //then
        // user 9_000 잔액 남아야
        assertThat(user.getAmount().getAmount()).isEqualTo(9_000L);

        // 1시간 주차권 1개에서 1개 썻으니 0개 되어야함, 2시간 주차권은 1개 그대로
        assertThat(user.getParkingTicket().getTwoHourParkingTicket()).isEqualTo(1);
        assertThat(user.getParkingTicket().getOneHourParkingTicket()).isEqualTo(0);
    }

    @DisplayName("1일, 2일 주차권 모두 있는 (경차, 2시간 50분 주차 -> 2시간 이상이니 2시간 주차권 사용 : 50분 주차 -> 1,000원 -> 경차이니 500원 -> user는 잔액 9,500)")
    @Test
    public void newPaymentPolicyPayTest4() {
        money = new Money(10_000L);
        // 2시간 주차권 1개, 1시간 주차권 1개 있음
        ParkingTicket parkingTicket = new ParkingTicket(1, 1);
        user = new User("jo", money, lightCar, false, parkingTicket);

        when(parkingLot.findUserByCar(lightCar)).thenReturn(user);

        parkingSpace = new ParkingSpace("A-1", false, car);
        parkingSpace.setStartParkDateTime(LocalDateTime.now());
        parkingSpace.setEndParkDateTime(parkingSpace.getStartParkDateTime().plusHours(2).plusMinutes(50));

        when(parkingLot.findParkingSpaceByNumber(car.getNumber())).thenReturn(parkingSpace);
        exit = new Exit(parkingLot, user); // parkingLotd의 stubbing 지정 이후 exit을 생성해야 (parkingLot을 생성자 인자로 가지기 때문)

        // when
        long parkingPayment = exit.newPaymentPolicyPay(user);
        user.pay(parkingPayment);

        //then
        // user 9_500 잔액 남아야
        assertThat(user.getAmount().getAmount()).isEqualTo(9_500L);

        // 2시간 주차권 1개에서 1개 썻으니 0개 되어야함, 1시간 주차권은 1개 그대로
        assertThat(user.getParkingTicket().getTwoHourParkingTicket()).isEqualTo(0);
        assertThat(user.getParkingTicket().getOneHourParkingTicket()).isEqualTo(1);
    }

}
