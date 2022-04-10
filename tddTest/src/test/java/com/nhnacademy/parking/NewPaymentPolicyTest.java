package com.nhnacademy.parking;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class NewPaymentPolicyTest {
    private ParkingSystem parkingSystem;
    private Exit exit;
    private ParkingLot parkingLot;
    private Car car;
    private Car lightCar;
    private ParkingSpace parkingSpace;
    private ParkingSpace parkingSpace2;
    private ParkingTicket parkingTicket;
    private User user;
    private User user2;

    @BeforeEach
    void setUp() {
        parkingSystem = new ParkingSystem(parkingLot);
        parkingLot = mock(ParkingLot.class);

        car = new Car("1111", CarType.REGULAR_CAR);
        boolean available = false;
        String code = "A-1";

        user = new User("jo", null, car, false, new ParkingTicket(0, 0));

        when(parkingLot.findUserByCar(car)).thenReturn(user);

        parkingSpace = new ParkingSpace(code, available, car);
        parkingSpace.setStartParkDateTime(LocalDateTime.now());

        lightCar = new Car("1111", CarType.LIGHT_CAR);
        parkingSpace2 = new ParkingSpace(code, available, lightCar);
        parkingSpace2.setStartParkDateTime(LocalDateTime.now());

        user2 = new User("jo", null, lightCar, false, new ParkingTicket(0, 0));

        when(parkingLot.findUserByCar(lightCar)).thenReturn(user2);

    }

    @DisplayName("주차장에서 나갈때 요금표 대로 결제가 되는지 확인 (일반 차, 30분 1초 주차 -> 1,000원)")
    @Test
    public void newPaymentPolicyPayTest1() {
        parkingSpace.setEndParkDateTime(parkingSpace.getStartParkDateTime().plusMinutes(30).plusSeconds(1));

        when(parkingLot.findParkingSpaceByNumber(car.getNumber())).thenReturn(parkingSpace);
        exit = new Exit(parkingLot, user); // parkingLotd의 stubbing 지정 이후 exit을 생성해야 (parkingLot을 생성자 인자로 가지기 때문)

        // when
        Car exitCar = new Car("1111", CarType.REGULAR_CAR);

        // then
        assertThat(exit.newPaymentPolicyPay(user)).isEqualTo(1_000L);

        //결제 후 해당 parkingSpace는 이용가능하고 차가 존재하면 안되니(null) 그것 또한 테스트
        assertThat(parkingSpace.isAvailable()).isTrue();
        assertThat(parkingSpace.getCar()).isNull();
    }

    @DisplayName("주차장에서 나갈때 요금표 대로 결제가 되는지 확인 (일반 차 30분 주차 -> 0원)")
    @Test
    public void parKingPaymentPayTest1_2() {
        parkingSpace.setEndParkDateTime(parkingSpace.getStartParkDateTime().plusMinutes(30));

        when(parkingLot.findParkingSpaceByNumber(car.getNumber())).thenReturn(parkingSpace);
        exit = new Exit(parkingLot, user); // parkingLotd의 stubbing 지정 이후 exit을 생성해야 (parkingLot을 생성자 인자로 가지기 때문)

        // when
        Car exitCar = new Car("1111", CarType.REGULAR_CAR);

        // then
        assertThat(exit.newPaymentPolicyPay(user)).isEqualTo(0L);

        //결제 후 해당 parkingSpace는 이용가능하고 차가 존재하면 안되니(null) 그것 또한 테스트
        assertThat(parkingSpace.isAvailable()).isTrue();
        assertThat(parkingSpace.getCar()).isNull();
    }

    @DisplayName("주차장에서 나갈때 요금표 대로 결제가 되는지 확인 (일반 차 29분 주차 -> 0원)")
    @Test
    public void parKingPaymentPayTest1_3() {
        parkingSpace.setEndParkDateTime(parkingSpace.getStartParkDateTime().plusMinutes(29));

        when(parkingLot.findParkingSpaceByNumber(car.getNumber())).thenReturn(parkingSpace);
        exit = new Exit(parkingLot, user); // parkingLotd의 stubbing 지정 이후 exit을 생성해야 (parkingLot을 생성자 인자로 가지기 때문)

        // when
        Car exitCar = new Car("1111", CarType.REGULAR_CAR);

        // then
        assertThat(exit.newPaymentPolicyPay(user)).isEqualTo(0L);

        //결제 후 해당 parkingSpace는 이용가능하고 차가 존재하면 안되니(null) 그것 또한 테스트
        assertThat(parkingSpace.isAvailable()).isTrue();
        assertThat(parkingSpace.getCar()).isNull();
    }

    @DisplayName("주차장에서 나갈때 요금표 대로 결제가 되는지 확인 (일반 차 59분 주차 -> 1,000원)")
    @Test
    public void parKingPaymentPayTest2() {
        parkingSpace.setEndParkDateTime(parkingSpace.getStartParkDateTime().plusMinutes(59));

        when(parkingLot.findParkingSpaceByNumber(car.getNumber())).thenReturn(parkingSpace);
        exit = new Exit(parkingLot, user); // parkingLotd의 stubbing 지정 이후 exit을 생성해야 (parkingLot을 생성자 인자로 가지기 때문)

        // when
        Car exitCar = new Car("1111", CarType.REGULAR_CAR);

        // then
        assertThat(exit.newPaymentPolicyPay(user)).isEqualTo(1_000L);

        //결제 후 해당 parkingSpace는 이용가능하고 차가 존재하면 안되니(null) 그것 또한 테스트
        assertThat(parkingSpace.isAvailable()).isTrue();
        assertThat(parkingSpace.getCar()).isNull();
    }

    @DisplayName("주차장에서 나갈때 요금표 대로 결제가 되는지 확인 (일반 차 1시간 주차 -> 1,000원)")
    @Test
    public void parKingPaymentPayTest2_2() {
        parkingSpace.setEndParkDateTime(parkingSpace.getStartParkDateTime().plusHours(1));

        when(parkingLot.findParkingSpaceByNumber(car.getNumber())).thenReturn(parkingSpace);
        exit = new Exit(parkingLot, user); // parkingLotd의 stubbing 지정 이후 exit을 생성해야 (parkingLot을 생성자 인자로 가지기 때문)

        // when
        Car exitCar = new Car("1111", CarType.REGULAR_CAR);

        // then
        assertThat(exit.newPaymentPolicyPay(user)).isEqualTo(1_000L);

        //결제 후 해당 parkingSpace는 이용가능하고 차가 존재하면 안되니(null) 그것 또한 테스트
        assertThat(parkingSpace.isAvailable()).isTrue();
        assertThat(parkingSpace.getCar()).isNull();
    }

    @DisplayName("주차장에서 나갈때 요금표 대로 결제가 되는지 확인 (일반 차 1시간 1초 주차 -> 1,500원)")
    @Test
    public void parKingPaymentPayTest2_3() {
        parkingSpace.setEndParkDateTime(parkingSpace.getStartParkDateTime().plusHours(1).plusSeconds(1));

        when(parkingLot.findParkingSpaceByNumber(car.getNumber())).thenReturn(parkingSpace);
        exit = new Exit(parkingLot, user); // parkingLotd의 stubbing 지정 이후 exit을 생성해야 (parkingLot을 생성자 인자로 가지기 때문)

        // when
        Car exitCar = new Car("1111", CarType.REGULAR_CAR);

        // then
        assertThat(exit.newPaymentPolicyPay(user)).isEqualTo(1_500L);

        //결제 후 해당 parkingSpace는 이용가능하고 차가 존재하면 안되니(null) 그것 또한 테스트
        assertThat(parkingSpace.isAvailable()).isTrue();
        assertThat(parkingSpace.getCar()).isNull();

    }

    @DisplayName("주차장에서 나갈때 요금표 대로 결제가 되는지 확인 (일반 차 6시간 주차 -> 15,000원)")
    @Test
    public void parKingPaymentPayTest3() {
        parkingSpace.setEndParkDateTime(parkingSpace.getStartParkDateTime().plusHours(6));

        when(parkingLot.findParkingSpaceByNumber(car.getNumber())).thenReturn(parkingSpace);
        exit = new Exit(parkingLot, user); // parkingLotd의 stubbing 지정 이후 exit을 생성해야 (parkingLot을 생성자 인자로 가지기 때문)

        // when
        Car exitCar = new Car("1111", CarType.REGULAR_CAR);

        // then
        assertThat(exit.newPaymentPolicyPay(user)).isEqualTo(15_000L);

        //결제 후 해당 parkingSpace는 이용가능하고 차가 존재하면 안되니(null) 그것 또한 테스트
        assertThat(parkingSpace.isAvailable()).isTrue();
        assertThat(parkingSpace.getCar()).isNull();
    }

    @DisplayName("주차장에서 나갈때 요금표 대로 결제가 되는지 확인 (일반 차 1일 -> 15,000원)")
    @Test
    public void parKingPaymentPayTest4() {
        parkingSpace.setEndParkDateTime(parkingSpace.getStartParkDateTime().plusDays(1));

        when(parkingLot.findParkingSpaceByNumber(car.getNumber())).thenReturn(parkingSpace);
        exit = new Exit(parkingLot, user); // parkingLotd의 stubbing 지정 이후 exit을 생성해야 (parkingLot을 생성자 인자로 가지기 때문)

        // when
        Car exitCar = new Car("1111", CarType.REGULAR_CAR);

        // then
        assertThat(exit.newPaymentPolicyPay(user)).isEqualTo(15_000L);

        //결제 후 해당 parkingSpace는 이용가능하고 차가 존재하면 안되니(null) 그것 또한 테스트
        assertThat(parkingSpace.isAvailable()).isTrue();
        assertThat(parkingSpace.getCar()).isNull();
    }

    @DisplayName("주차장에서 나갈때 요금표 대로 결제가 되는지 확인 (일반 차 1일 1초 -> 15,000원)")
    @Test
    public void parKingPaymentPayTest4_2() {
        parkingSpace.setEndParkDateTime(parkingSpace.getStartParkDateTime().plusDays(1).plusSeconds(1));

        when(parkingLot.findParkingSpaceByNumber(car.getNumber())).thenReturn(parkingSpace);
        exit = new Exit(parkingLot, user); // parkingLotd의 stubbing 지정 이후 exit을 생성해야 (parkingLot을 생성자 인자로 가지기 때문)

        // when
        Car exitCar = new Car("1111", CarType.REGULAR_CAR);

        // then
        assertThat(exit.newPaymentPolicyPay(user)).isEqualTo(15_000L);

        //결제 후 해당 parkingSpace는 이용가능하고 차가 존재하면 안되니(null) 그것 또한 테스트
        assertThat(parkingSpace.isAvailable()).isTrue();
        assertThat(parkingSpace.getCar()).isNull();
    }

    @DisplayName("주차장에서 나갈때 요금표 대로 결제가 되는지 확인 (일반 차 1일 30분 1초 -> 16,000원)")
    @Test
    public void parKingPaymentPayTest4_3() {
        parkingSpace.setEndParkDateTime(parkingSpace.getStartParkDateTime().plusDays(1).plusMinutes(30).plusSeconds(1));

        when(parkingLot.findParkingSpaceByNumber(car.getNumber())).thenReturn(parkingSpace);
        exit = new Exit(parkingLot, user); // parkingLotd의 stubbing 지정 이후 exit을 생성해야 (parkingLot을 생성자 인자로 가지기 때문)

        // when
        Car exitCar = new Car("1111", CarType.REGULAR_CAR);

        // then
        assertThat(exit.newPaymentPolicyPay(user)).isEqualTo(16_000L);

        //결제 후 해당 parkingSpace는 이용가능하고 차가 존재하면 안되니(null) 그것 또한 테스트
        assertThat(parkingSpace.isAvailable()).isTrue();
        assertThat(parkingSpace.getCar()).isNull();
    }

    @DisplayName("주차장에서 나갈때 요금표 대로 결제가 되는지 확인 (일반 차 1일 1시간 1초 -> 16,500원)")
    @Test
    public void parKingPaymentPayTest4_4() {
        parkingSpace.setEndParkDateTime(parkingSpace.getStartParkDateTime().plusDays(1).plusHours(1).plusSeconds(1));

        when(parkingLot.findParkingSpaceByNumber(car.getNumber())).thenReturn(parkingSpace);
        exit = new Exit(parkingLot, user); // parkingLotd의 stubbing 지정 이후 exit을 생성해야 (parkingLot을 생성자 인자로 가지기 때문)

        // when
        Car exitCar = new Car("1111", CarType.REGULAR_CAR);

        // then
        assertThat(exit.newPaymentPolicyPay(user)).isEqualTo(16_500L);

        //결제 후 해당 parkingSpace는 이용가능하고 차가 존재하면 안되니(null) 그것 또한 테스트
        assertThat(parkingSpace.isAvailable()).isTrue();
        assertThat(parkingSpace.getCar()).isNull();
    }

    @DisplayName("주차장에서 나갈때 요금표 대로 결제가 되는지 확인 (일반 차 1일 1시간 30분 1초 -> 18,000원)")
    @Test
    public void parKingPaymentPayTest4_5() {
        parkingSpace.setEndParkDateTime(parkingSpace.getStartParkDateTime().plusDays(1).plusHours(1).plusMinutes(30).plusSeconds(1));

        when(parkingLot.findParkingSpaceByNumber(car.getNumber())).thenReturn(parkingSpace);
        exit = new Exit(parkingLot, user); // parkingLotd의 stubbing 지정 이후 exit을 생성해야 (parkingLot을 생성자 인자로 가지기 때문)

        // when
        Car exitCar = new Car("1111", CarType.REGULAR_CAR);

        // then
        assertThat(exit.newPaymentPolicyPay(user)).isEqualTo(18_000L);

        //결제 후 해당 parkingSpace는 이용가능하고 차가 존재하면 안되니(null) 그것 또한 테스트
        assertThat(parkingSpace.isAvailable()).isTrue();
        assertThat(parkingSpace.getCar()).isNull();
    }

    @DisplayName("주차장에서 나갈때 요금표 대로 결제가 되는지 확인 (일반 차 2일 -> 30,000원)")
    @Test
    public void parKingPaymentPayTest5() {
        parkingSpace.setEndParkDateTime(parkingSpace.getStartParkDateTime().plusDays(2));

        when(parkingLot.findParkingSpaceByNumber(car.getNumber())).thenReturn(parkingSpace);
        exit = new Exit(parkingLot, user); // parkingLotd의 stubbing 지정 이후 exit을 생성해야 (parkingLot을 생성자 인자로 가지기 때문)

        // when
        Car exitCar = new Car("1111", CarType.REGULAR_CAR);

        // then
        assertThat(exit.newPaymentPolicyPay(user)).isEqualTo(30_000L);

        //결제 후 해당 parkingSpace는 이용가능하고 차가 존재하면 안되니(null) 그것 또한 테스트
        assertThat(parkingSpace.isAvailable()).isTrue();
        assertThat(parkingSpace.getCar()).isNull();
    }

    @DisplayName("주차장에서 나갈때 요금표 대로 결제가 되는지 확인 (일반 차 2일 61분 -> 31,500원)")
    @Test
    public void parKingPaymentPayTest5_2() {
        parkingSpace.setEndParkDateTime(parkingSpace.getStartParkDateTime().plusDays(2).plusMinutes(61));

        when(parkingLot.findParkingSpaceByNumber(car.getNumber())).thenReturn(parkingSpace);
        exit = new Exit(parkingLot, user); // parkingLotd의 stubbing 지정 이후 exit을 생성해야 (parkingLot을 생성자 인자로 가지기 때문)

        // when
        Car exitCar = new Car("1111", CarType.REGULAR_CAR);

        // then
        assertThat(exit.newPaymentPolicyPay(user)).isEqualTo(31_500L);

        //결제 후 해당 parkingSpace는 이용가능하고 차가 존재하면 안되니(null) 그것 또한 테스트
        assertThat(parkingSpace.isAvailable()).isTrue();
        assertThat(parkingSpace.getCar()).isNull();
    }

    @DisplayName("주차장에서 나갈때 요금표 대로 결제가 되는지 확인 (일반 차 5일 6시간 주차 -> 90,000원)")
    @Test
    public void parKingPaymentPayTest6() {
        parkingSpace.setEndParkDateTime(parkingSpace.getStartParkDateTime().plusDays(5).plusHours(6));

        when(parkingLot.findParkingSpaceByNumber(car.getNumber())).thenReturn(parkingSpace);
        exit = new Exit(parkingLot, user); // parkingLotd의 stubbing 지정 이후 exit을 생성해야 (parkingLot을 생성자 인자로 가지기 때문)

        // when
        Car exitCar = new Car("1111", CarType.REGULAR_CAR);

        // then
        assertThat(exit.newPaymentPolicyPay(user)).isEqualTo(90_000L);

        //결제 후 해당 parkingSpace는 이용가능하고 차가 존재하면 안되니(null) 그것 또한 테스트
        assertThat(parkingSpace.isAvailable()).isTrue();
        assertThat(parkingSpace.getCar()).isNull();
    }

    @DisplayName("주차장에서 나갈때 요금표 대로 결제가 되는지 확인 (경차, 30분 1초 주차 -> 500원)")
    @Test
    public void newPaymentPolicyPayLightCarTest1() {
        parkingSpace2.setEndParkDateTime(parkingSpace2.getStartParkDateTime().plusMinutes(30).plusSeconds(1));

        when(parkingLot.findParkingSpaceByNumber(car.getNumber())).thenReturn(parkingSpace2);
        exit = new Exit(parkingLot, user2); // parkingLotd의 stubbing 지정 이후 exit을 생성해야 (parkingLot을 생성자 인자로 가지기 때문)

        // when
        Car exitCar = new Car("1111", CarType.LIGHT_CAR);

        // then
        assertThat(exit.newPaymentPolicyPay(user2)).isEqualTo(500L);

        //결제 후 해당 parkingSpace는 이용가능하고 차가 존재하면 안되니(null) 그것 또한 테스트
        assertThat(parkingSpace2.isAvailable()).isTrue();
        assertThat(parkingSpace2.getCar()).isNull();
    }

    @DisplayName("주차장에서 나갈때 요금표 대로 결제가 되는지 확인 (경차 30분 주차 -> 0원)")
    @Test
    public void parKingPaymentPayLightCarTest1_2() {
        parkingSpace2.setEndParkDateTime(parkingSpace2.getStartParkDateTime().plusMinutes(30));

        when(parkingLot.findParkingSpaceByNumber(car.getNumber())).thenReturn(parkingSpace2);
        exit = new Exit(parkingLot, user2); // parkingLotd의 stubbing 지정 이후 exit을 생성해야 (parkingLot을 생성자 인자로 가지기 때문)

        // when
        Car exitCar = new Car("1111", CarType.LIGHT_CAR);

        // then
        assertThat(exit.newPaymentPolicyPay(user2)).isEqualTo(0L);

        //결제 후 해당 parkingSpace는 이용가능하고 차가 존재하면 안되니(null) 그것 또한 테스트
        assertThat(parkingSpace2.isAvailable()).isTrue();
        assertThat(parkingSpace2.getCar()).isNull();
    }

    @DisplayName("주차장에서 나갈때 요금표 대로 결제가 되는지 확인 (경차 29분 주차 -> 0원)")
    @Test
    public void parKingPaymentPayLightCarTest1_3() {
        parkingSpace2.setEndParkDateTime(parkingSpace2.getStartParkDateTime().plusMinutes(29));

        when(parkingLot.findParkingSpaceByNumber(car.getNumber())).thenReturn(parkingSpace2);
        exit = new Exit(parkingLot, user2); // parkingLotd의 stubbing 지정 이후 exit을 생성해야 (parkingLot을 생성자 인자로 가지기 때문)

        // when
        Car exitCar = new Car("1111", CarType.LIGHT_CAR);

        // then
        assertThat(exit.newPaymentPolicyPay(user2)).isEqualTo(0L);

        //결제 후 해당 parkingSpace는 이용가능하고 차가 존재하면 안되니(null) 그것 또한 테스트
        assertThat(parkingSpace2.isAvailable()).isTrue();
        assertThat(parkingSpace2.getCar()).isNull();
    }

    @DisplayName("주차장에서 나갈때 요금표 대로 결제가 되는지 확인 (경차 59분 주차 -> 500원)")
    @Test
    public void parKingPaymentPayLightCarTest2() {
        parkingSpace2.setEndParkDateTime(parkingSpace2.getStartParkDateTime().plusMinutes(59));

        when(parkingLot.findParkingSpaceByNumber(car.getNumber())).thenReturn(parkingSpace2);
        exit = new Exit(parkingLot, user2); // parkingLotd의 stubbing 지정 이후 exit을 생성해야 (parkingLot을 생성자 인자로 가지기 때문)

        // when
        Car exitCar = new Car("1111", CarType.LIGHT_CAR);

        // then
        assertThat(exit.newPaymentPolicyPay(user2)).isEqualTo(500L);

        //결제 후 해당 parkingSpace는 이용가능하고 차가 존재하면 안되니(null) 그것 또한 테스트
        assertThat(parkingSpace2.isAvailable()).isTrue();
        assertThat(parkingSpace2.getCar()).isNull();

    }

    @DisplayName("주차장에서 나갈때 요금표 대로 결제가 되는지 확인 (경차 1시간 주차 -> 500원)")
    @Test
    public void parKingPaymentPayLightCarTest2_2() {
        parkingSpace2.setEndParkDateTime(parkingSpace2.getStartParkDateTime().plusHours(1));

        when(parkingLot.findParkingSpaceByNumber(car.getNumber())).thenReturn(parkingSpace2);
        exit = new Exit(parkingLot, user2); // parkingLotd의 stubbing 지정 이후 exit을 생성해야 (parkingLot을 생성자 인자로 가지기 때문)

        // when
        Car exitCar = new Car("1111", CarType.LIGHT_CAR);

        // then
        assertThat(exit.newPaymentPolicyPay(user2)).isEqualTo(500L);

        //결제 후 해당 parkingSpace는 이용가능하고 차가 존재하면 안되니(null) 그것 또한 테스트
        assertThat(parkingSpace2.isAvailable()).isTrue();
        assertThat(parkingSpace2.getCar()).isNull();

    }

    @DisplayName("주차장에서 나갈때 요금표 대로 결제가 되는지 확인 (경차 1시간 1초 주차 -> 750원)")
    @Test
    public void parKingPaymentPayLightCarTest2_3() {
        parkingSpace2.setEndParkDateTime(parkingSpace2.getStartParkDateTime().plusHours(1).plusSeconds(1));

        when(parkingLot.findParkingSpaceByNumber(car.getNumber())).thenReturn(parkingSpace2);
        exit = new Exit(parkingLot, user2); // parkingLotd의 stubbing 지정 이후 exit을 생성해야 (parkingLot을 생성자 인자로 가지기 때문)

        // when
        Car exitCar = new Car("1111", CarType.LIGHT_CAR);

        // then
        assertThat(exit.newPaymentPolicyPay(user2)).isEqualTo(750L);

        //결제 후 해당 parkingSpace는 이용가능하고 차가 존재하면 안되니(null) 그것 또한 테스트
        assertThat(parkingSpace2.isAvailable()).isTrue();
        assertThat(parkingSpace2.getCar()).isNull();

    }

    @DisplayName("주차장에서 나갈때 요금표 대로 결제가 되는지 확인 (경차 6시간 주차 -> 7,500원)")
    @Test
    public void parKingPaymentPayLightCarTest3() {
        parkingSpace2.setEndParkDateTime(parkingSpace2.getStartParkDateTime().plusHours(6));

        when(parkingLot.findParkingSpaceByNumber(car.getNumber())).thenReturn(parkingSpace2);
        exit = new Exit(parkingLot, user2); // parkingLotd의 stubbing 지정 이후 exit을 생성해야 (parkingLot을 생성자 인자로 가지기 때문)

        // when
        Car exitCar = new Car("1111", CarType.LIGHT_CAR);

        // then
        assertThat(exit.newPaymentPolicyPay(user2)).isEqualTo(7_500L);

        //결제 후 해당 parkingSpace는 이용가능하고 차가 존재하면 안되니(null) 그것 또한 테스트
        assertThat(parkingSpace2.isAvailable()).isTrue();
        assertThat(parkingSpace2.getCar()).isNull();

    }

    @DisplayName("주차장에서 나갈때 요금표 대로 결제가 되는지 확인 (경차 1일 -> 7,500원)")
    @Test
    public void parKingPaymentPayLightCarTest4() {
        parkingSpace2.setEndParkDateTime(parkingSpace2.getStartParkDateTime().plusDays(1));

        when(parkingLot.findParkingSpaceByNumber(car.getNumber())).thenReturn(parkingSpace2);
        exit = new Exit(parkingLot, user2); // parkingLotd의 stubbing 지정 이후 exit을 생성해야 (parkingLot을 생성자 인자로 가지기 때문)

        // when
        Car exitCar = new Car("1111", CarType.LIGHT_CAR);

        // then
        assertThat(exit.newPaymentPolicyPay(user2)).isEqualTo(7_500L);

        //결제 후 해당 parkingSpace는 이용가능하고 차가 존재하면 안되니(null) 그것 또한 테스트
        assertThat(parkingSpace2.isAvailable()).isTrue();
        assertThat(parkingSpace2.getCar()).isNull();

    }

    @DisplayName("주차장에서 나갈때 요금표 대로 결제가 되는지 확인 (경차 1일 1초 -> 7,500원)")
    @Test
    public void parKingPaymentPayLightCarTest4_2() {
        parkingSpace2.setEndParkDateTime(parkingSpace2.getStartParkDateTime().plusDays(1).plusSeconds(1));

        when(parkingLot.findParkingSpaceByNumber(car.getNumber())).thenReturn(parkingSpace2);
        exit = new Exit(parkingLot, user2); // parkingLotd의 stubbing 지정 이후 exit을 생성해야 (parkingLot을 생성자 인자로 가지기 때문)

        // when
        Car exitCar = new Car("1111", CarType.LIGHT_CAR);

        // then
        assertThat(exit.newPaymentPolicyPay(user2)).isEqualTo(7_500L);

        //결제 후 해당 parkingSpace는 이용가능하고 차가 존재하면 안되니(null) 그것 또한 테스트
        assertThat(parkingSpace2.isAvailable()).isTrue();
        assertThat(parkingSpace2.getCar()).isNull();

    }

    @DisplayName("주차장에서 나갈때 요금표 대로 결제가 되는지 확인 (경차 1일 30분 1초 -> 8,000원)")
    @Test
    public void parKingPaymentPayLightCarTest4_3() {
        parkingSpace2.setEndParkDateTime(parkingSpace2.getStartParkDateTime().plusDays(1).plusMinutes(30).plusSeconds(1));

        when(parkingLot.findParkingSpaceByNumber(car.getNumber())).thenReturn(parkingSpace2);
        exit = new Exit(parkingLot, user2); // parkingLotd의 stubbing 지정 이후 exit을 생성해야 (parkingLot을 생성자 인자로 가지기 때문)

        // when
        Car exitCar = new Car("1111", CarType.LIGHT_CAR);

        // then
        assertThat(exit.newPaymentPolicyPay(user2)).isEqualTo(8_000L);

        //결제 후 해당 parkingSpace는 이용가능하고 차가 존재하면 안되니(null) 그것 또한 테스트
        assertThat(parkingSpace2.isAvailable()).isTrue();
        assertThat(parkingSpace2.getCar()).isNull();

    }

    @DisplayName("주차장에서 나갈때 요금표 대로 결제가 되는지 확인 (경차 1일 1시간 1초 -> 8,250원)")
    @Test
    public void parKingPaymentPayLightCarTest4_4() {
        parkingSpace2.setEndParkDateTime(parkingSpace2.getStartParkDateTime().plusDays(1).plusHours(1).plusSeconds(1));

        when(parkingLot.findParkingSpaceByNumber(car.getNumber())).thenReturn(parkingSpace2);
        exit = new Exit(parkingLot, user2); // parkingLotd의 stubbing 지정 이후 exit을 생성해야 (parkingLot을 생성자 인자로 가지기 때문)

        // when
        Car exitCar = new Car("1111", CarType.LIGHT_CAR);

        // then
        assertThat(exit.newPaymentPolicyPay(user2)).isEqualTo(8_250L);

        //결제 후 해당 parkingSpace는 이용가능하고 차가 존재하면 안되니(null) 그것 또한 테스트
        assertThat(parkingSpace2.isAvailable()).isTrue();
        assertThat(parkingSpace2.getCar()).isNull();

    }

    @DisplayName("주차장에서 나갈때 요금표 대로 결제가 되는지 확인 (경차 1일 1시간 30분 1초 -> 9,000원)")
    @Test
    public void parKingPaymentPayLightCarTest4_5() {
        parkingSpace2.setEndParkDateTime(parkingSpace2.getStartParkDateTime().plusDays(1).plusHours(1).plusMinutes(30).plusSeconds(1));

        when(parkingLot.findParkingSpaceByNumber(car.getNumber())).thenReturn(parkingSpace2);
        exit = new Exit(parkingLot, user2); // parkingLotd의 stubbing 지정 이후 exit을 생성해야 (parkingLot을 생성자 인자로 가지기 때문)

        // when
        Car exitCar = new Car("1111", CarType.LIGHT_CAR);

        // then
        assertThat(exit.newPaymentPolicyPay(user2)).isEqualTo(9_000L);

        //결제 후 해당 parkingSpace는 이용가능하고 차가 존재하면 안되니(null) 그것 또한 테스트
        assertThat(parkingSpace2.isAvailable()).isTrue();
        assertThat(parkingSpace2.getCar()).isNull();

    }

    @DisplayName("주차장에서 나갈때 요금표 대로 결제가 되는지 확인 (경차 2일 -> 15,000원)")
    @Test
    public void parKingPaymentPayLightCarTest5() {
        parkingSpace2.setEndParkDateTime(parkingSpace2.getStartParkDateTime().plusDays(2));

        when(parkingLot.findParkingSpaceByNumber(car.getNumber())).thenReturn(parkingSpace2);
        exit = new Exit(parkingLot, user2); // parkingLotd의 stubbing 지정 이후 exit을 생성해야 (parkingLot을 생성자 인자로 가지기 때문)

        // when
        Car exitCar = new Car("1111", CarType.LIGHT_CAR);

        // then
        assertThat(exit.newPaymentPolicyPay(user2)).isEqualTo(15_000L);

        //결제 후 해당 parkingSpace는 이용가능하고 차가 존재하면 안되니(null) 그것 또한 테스트
        assertThat(parkingSpace2.isAvailable()).isTrue();
        assertThat(parkingSpace2.getCar()).isNull();

    }

    @DisplayName("주차장에서 나갈때 요금표 대로 결제가 되는지 확인 (경차 2일 61분 -> 15,750원)")
    @Test
    public void parKingPaymentPayLightCarTest5_2() {
        parkingSpace2.setEndParkDateTime(parkingSpace2.getStartParkDateTime().plusDays(2).plusMinutes(61));

        when(parkingLot.findParkingSpaceByNumber(car.getNumber())).thenReturn(parkingSpace2);
        exit = new Exit(parkingLot, user2); // parkingLotd의 stubbing 지정 이후 exit을 생성해야 (parkingLot을 생성자 인자로 가지기 때문)

        // when
        Car exitCar = new Car("1111", CarType.LIGHT_CAR);

        // then
        assertThat(exit.newPaymentPolicyPay(user2)).isEqualTo(15_750L);

        //결제 후 해당 parkingSpace는 이용가능하고 차가 존재하면 안되니(null) 그것 또한 테스트
        assertThat(parkingSpace2.isAvailable()).isTrue();
        assertThat(parkingSpace2.getCar()).isNull();

    }

    @DisplayName("주차장에서 나갈때 요금표 대로 결제가 되는지 확인 (경차 5일 6시간 주차 -> 45,000원)")
    @Test
    public void parKingPaymentPayLightCarTest6() {
        parkingSpace2.setEndParkDateTime(parkingSpace2.getStartParkDateTime().plusDays(5).plusHours(6));

        when(parkingLot.findParkingSpaceByNumber(car.getNumber())).thenReturn(parkingSpace2);
        exit = new Exit(parkingLot, user2); // parkingLotd의 stubbing 지정 이후 exit을 생성해야 (parkingLot을 생성자 인자로 가지기 때문)

        // when
        Car exitCar = new Car("1111", CarType.LIGHT_CAR);

        // then
        assertThat(exit.newPaymentPolicyPay(user2)).isEqualTo(45_000L);

        //결제 후 해당 parkingSpace는 이용가능하고 차가 존재하면 안되니(null) 그것 또한 테스트
        assertThat(parkingSpace2.isAvailable()).isTrue();
        assertThat(parkingSpace2.getCar()).isNull();

    }
}
