package com.nhnacademy.parking;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.nhnacademy.parking.exception.InvalidCodeException;
import com.nhnacademy.parking.exception.InvalidParkingException;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ParkingLotServiceTest {
    private ParkingSystem parkingSystem;
    private Entrance entrance;
    private Exit exit;
    private ParkingLot parkingLot;
    private Car car;
    private ParkingSpace parkingSpace;

    private ParkingTicket parkingTicket;

    @BeforeEach
    void setUp() {
        parkingSystem = new ParkingSystem(parkingLot);
        entrance = new Entrance(parkingLot);
        parkingLot = mock(ParkingLot.class);

        car = new Car("1111", null);
        boolean available = false;
        String code = "A-1";

        parkingSpace = new ParkingSpace(code, available, car);
        parkingSpace.setStartParkDateTime(LocalDateTime.now());
    }

    @DisplayName("주차장에 들어오는 차의 번호판을 잘 인식하는지 테스트")
    @Test
    public void checkingCarNumberScanTest() {
        // when
        String number = "8282";
        Car scannedCar = new Car(number, CarType.REGULAR_CAR);
        String scannedNumber = entrance.scan(scannedCar);

        //then
        assertThat(scannedNumber).isEqualTo(number);

    }


    @DisplayName("주차장에서 코드를 이용해 주차구역 정보 얻기 (해당 코드 없을시 에러)")
    @Test
    public void findParkingSpaceByCodeTest() {
        String code = "A-1";
        Car car = new Car("1111", null);
        ParkingSpace parkingSpace = new ParkingSpace(code, true, car);

        when(parkingLot.findParkingSpaceByCode(code)).thenReturn(parkingSpace);

        // when
        String findSpaceCode = "A-1";
        ParkingSpace findedParkingSpace = parkingLot.findParkingSpaceByCode(findSpaceCode);

        //then
        assertThat(findedParkingSpace).isEqualTo(parkingSpace);

    }

    @DisplayName("주차장에서 존재하지 않는 코드로 주차구역 정보를 얻는경우 에러 처리")
    @Test
    public void findParkingSpaceByCodeFailTest() {
        String code = "잘못된 코드";

        when(parkingLot.findParkingSpaceByCode(code)).thenThrow(InvalidCodeException.class);

        // when
        String findSpaceCode = "잘못된 코드";
        //then
        assertThatThrownBy(() -> parkingLot.findParkingSpaceByCode(findSpaceCode));
    }


    @DisplayName("user가 차를 A-1에 주차함")
    @Test
    public void parkTheCarInTheA1Test() {
        String code = "A-1";
        boolean available = true;
        ParkingSpace parkingSpace = new ParkingSpace(code, available, null);

        when(parkingLot.findParkingSpaceByCode(code)).thenReturn(parkingSpace);

        // when
        Car parkingCar = new Car("1111", null);
        User parkingUser = new User("jo", null, parkingCar, false, null);
        String parkingCarCode ="A-1";


        // then
        assertThat(parkingUser.parkingToParkingLot(parkingCarCode, parkingCar, parkingLot)).isTrue();

        // 주차를 했으니 해당 주차공간(parkingSpace)에 available이 false이고 car에 parkingCar가 들어있어야 함
        assertThat(parkingSpace.isAvailable()).isFalse();
        assertThat(parkingSpace.getCar()).isEqualTo(parkingCar);

    }

    @DisplayName("user가 A-1에 주차하려할 때 이미 다른 차가 해당 장소에 주차한 경우 에러발생 테스트")
    @Test
    public void parkTheCarInTheA1FailTest() {
        String code = "A-1";
        boolean available = false;
        Car differentCar = new Car("2222", null);
        ParkingSpace parkingSpace = new ParkingSpace(code, available, differentCar);

        when(parkingLot.findParkingSpaceByCode(code)).thenReturn(parkingSpace);

        // when
        Car parkingCar = new Car("1111", null);
        User parkingUser = new User("jo", null, parkingCar, false, null);
        String parkingCarCode ="A-1";


        // then
        assertThatThrownBy(() -> parkingUser.parkingToParkingLot(parkingCarCode, parkingCar, parkingLot))
            .isInstanceOf(InvalidParkingException.class)
                .hasMessage("Invalid Parking");
    }

    @DisplayName("Exit에서 해당 차를 가진 User를 찾는지 테스트")
    @Test
    void carNumberCheckCarUser() {
        Car car = new Car("1111", null);
        User user = new User("jo", null, car, false, null);
        exit = new Exit(parkingLot, null);

        when(parkingLot.findUserByCar(car)).thenReturn(user);

        // when
        Car exitCar = new Car("1111", null);

        //then
        assertThat(exit.findUserByCar(exitCar)).isEqualTo(user);
    }

    @DisplayName("주차장에서 나갈때 요금표 대로 결제가 되는지 확인 (30분 1초 주차 -> 1,500원)")
    @Test
    public void parKingPaymentPayTest1() {
        parkingSpace.setEndParkDateTime(parkingSpace.getStartParkDateTime().plusMinutes(30).plusSeconds(1));

        when(parkingLot.findParkingSpaceByNumber(car.getNumber())).thenReturn(parkingSpace);
        exit = new Exit(parkingLot, null); // parkingLotd의 stubbing 지정 이후 exit을 생성해야 (parkingLot을 생성자 인자로 가지기 때문)

        // when
        Car exitCar = new Car("1111", null);

        // then
        assertThat(exit.pay(exitCar)).isEqualTo(1_500L);

        //결제 후 해당 parkingSpace는 이용가능하고 차가 존재하면 안되니(null) 그것 또한 테스트
        assertThat(parkingSpace.isAvailable()).isTrue();
        assertThat(parkingSpace.getCar()).isNull();
    }

    @DisplayName("주차장에서 나갈때 요금표 대로 결제가 되는지 확인 (30분 주차 -> 1,000원)")
    @Test
    public void parKingPaymentPayTest1_2() {
        parkingSpace.setEndParkDateTime(parkingSpace.getStartParkDateTime().plusMinutes(30));

        when(parkingLot.findParkingSpaceByNumber(car.getNumber())).thenReturn(parkingSpace);
        exit = new Exit(parkingLot, null); // parkingLotd의 stubbing 지정 이후 exit을 생성해야 (parkingLot을 생성자 인자로 가지기 때문)

        // when
        Car exitCar = new Car("1111", null);

        // then
        assertThat(exit.pay(exitCar)).isEqualTo(1_000L);

        //결제 후 해당 parkingSpace는 이용가능하고 차가 존재하면 안되니(null) 그것 또한 테스트
        assertThat(parkingSpace.isAvailable()).isTrue();
        assertThat(parkingSpace.getCar()).isNull();
    }

    @DisplayName("주차장에서 나갈때 요금표 대로 결제가 되는지 확인 (29분 주차 -> 1,000원)")
    @Test
    public void parKingPaymentPayTest1_3() {
        parkingSpace.setEndParkDateTime(parkingSpace.getStartParkDateTime().plusMinutes(29));

        when(parkingLot.findParkingSpaceByNumber(car.getNumber())).thenReturn(parkingSpace);
        exit = new Exit(parkingLot, null); // parkingLotd의 stubbing 지정 이후 exit을 생성해야 (parkingLot을 생성자 인자로 가지기 때문)

        // when
        Car exitCar = new Car("1111", null);

        // then
        assertThat(exit.pay(exitCar)).isEqualTo(1_000L);

        //결제 후 해당 parkingSpace는 이용가능하고 차가 존재하면 안되니(null) 그것 또한 테스트
        assertThat(parkingSpace.isAvailable()).isTrue();
        assertThat(parkingSpace.getCar()).isNull();
    }

    @DisplayName("주차장에서 나갈때 요금표 대로 결제가 되는지 확인 (50분 주차 -> 2,000원)")
    @Test
    public void parKingPaymentPayTest2() {
        parkingSpace.setEndParkDateTime(parkingSpace.getStartParkDateTime().plusMinutes(50));

        when(parkingLot.findParkingSpaceByNumber(car.getNumber())).thenReturn(parkingSpace);
        exit = new Exit(parkingLot, null); // parkingLotd의 stubbing 지정 이후 exit을 생성해야 (parkingLot을 생성자 인자로 가지기 때문)

        // when
        Car exitCar = new Car("1111", null);

        // then
        assertThat(exit.pay(exitCar)).isEqualTo(2_000L);

        //결제 후 해당 parkingSpace는 이용가능하고 차가 존재하면 안되니(null) 그것 또한 테스트
        assertThat(parkingSpace.isAvailable()).isTrue();
        assertThat(parkingSpace.getCar()).isNull();
    }

    @DisplayName("주차장에서 나갈때 요금표 대로 결제가 되는지 확인 (61분 주차 -> 3,000원)")
    @Test
    public void parKingPaymentPayTest3() {
        parkingSpace.setEndParkDateTime(parkingSpace.getStartParkDateTime().plusMinutes(61));

        when(parkingLot.findParkingSpaceByNumber(car.getNumber())).thenReturn(parkingSpace);
        exit = new Exit(parkingLot, null); // parkingLotd의 stubbing 지정 이후 exit을 생성해야 (parkingLot을 생성자 인자로 가지기 때문)

        // when
        Car exitCar = new Car("1111", null);

        // then
        assertThat(exit.pay(exitCar)).isEqualTo(3_000L);

        //결제 후 해당 parkingSpace는 이용가능하고 차가 존재하면 안되니(null) 그것 또한 테스트
        assertThat(parkingSpace.isAvailable()).isTrue();
        assertThat(parkingSpace.getCar()).isNull();
    }


    @DisplayName("주차장에서 나갈때 요금표 대로 결제가 되는지 확인 (6시간 주차 -> 10,000원)")
    @Test
    public void parKingPaymentPayTest4() {
        parkingSpace.setEndParkDateTime(parkingSpace.getStartParkDateTime().plusHours(6));

        when(parkingLot.findParkingSpaceByNumber(car.getNumber())).thenReturn(parkingSpace);
        exit = new Exit(parkingLot, null); // parkingLotd의 stubbing 지정 이후 exit을 생성해야 (parkingLot을 생성자 인자로 가지기 때문)

        // when
        Car exitCar = new Car("1111", null);

        // then
        assertThat(exit.pay(exitCar)).isEqualTo(10_000L);

        //결제 후 해당 parkingSpace는 이용가능하고 차가 존재하면 안되니(null) 그것 또한 테스트
        assertThat(parkingSpace.isAvailable()).isTrue();
        assertThat(parkingSpace.getCar()).isNull();
    }

    @DisplayName("주차장에서 나갈때 요금표 대로 결제가 되는지 확인 (1일 -> 10,000원)")
    @Test
    public void parKingPaymentPayTest5() {
        parkingSpace.setEndParkDateTime(parkingSpace.getStartParkDateTime().plusDays(1));

        when(parkingLot.findParkingSpaceByNumber(car.getNumber())).thenReturn(parkingSpace);
        exit = new Exit(parkingLot, null); // parkingLotd의 stubbing 지정 이후 exit을 생성해야 (parkingLot을 생성자 인자로 가지기 때문)

        // when
        Car exitCar = new Car("1111", null);

        // then
        assertThat(exit.pay(exitCar)).isEqualTo(10_000L);

        //결제 후 해당 parkingSpace는 이용가능하고 차가 존재하면 안되니(null) 그것 또한 테스트
        assertThat(parkingSpace.isAvailable()).isTrue();
        assertThat(parkingSpace.getCar()).isNull();
    }

    @DisplayName("주차장에서 나갈때 요금표 대로 결제가 되는지 확인 (1일 1초 -> 11,000원)")
    @Test
    public void parKingPaymentPayTest5_2() {
        parkingSpace.setEndParkDateTime(parkingSpace.getStartParkDateTime().plusDays(1).plusMinutes(1));

        when(parkingLot.findParkingSpaceByNumber(car.getNumber())).thenReturn(parkingSpace);
        exit = new Exit(parkingLot, null); // parkingLotd의 stubbing 지정 이후 exit을 생성해야 (parkingLot을 생성자 인자로 가지기 때문)

        // when
        Car exitCar = new Car("1111", null);

        // then
        assertThat(exit.pay(exitCar)).isEqualTo(11_000L);

        //결제 후 해당 parkingSpace는 이용가능하고 차가 존재하면 안되니(null) 그것 또한 테스트
        assertThat(parkingSpace.isAvailable()).isTrue();
        assertThat(parkingSpace.getCar()).isNull();
    }

    @DisplayName("주차장에서 나갈때 요금표 대로 결제가 되는지 확인 (2일 -> 20,000원)")
    @Test
    public void parKingPaymentPayTest6() {
        parkingSpace.setEndParkDateTime(parkingSpace.getStartParkDateTime().plusDays(2));

        when(parkingLot.findParkingSpaceByNumber(car.getNumber())).thenReturn(parkingSpace);
        exit = new Exit(parkingLot, null); // parkingLotd의 stubbing 지정 이후 exit을 생성해야 (parkingLot을 생성자 인자로 가지기 때문)

        // when
        Car exitCar = new Car("1111", null);

        // then
        assertThat(exit.pay(exitCar)).isEqualTo(20_000L);

        //결제 후 해당 parkingSpace는 이용가능하고 차가 존재하면 안되니(null) 그것 또한 테스트
        assertThat(parkingSpace.isAvailable()).isTrue();
        assertThat(parkingSpace.getCar()).isNull();
    }

    @DisplayName("주차장에서 나갈때 요금표 대로 결제가 되는지 확인 (2일 61분 -> 23,000원)")
    @Test
    public void parKingPaymentPayTest6_2() {
        parkingSpace.setEndParkDateTime(parkingSpace.getStartParkDateTime().plusDays(2).plusMinutes(61));

        when(parkingLot.findParkingSpaceByNumber(car.getNumber())).thenReturn(parkingSpace);
        exit = new Exit(parkingLot, null); // parkingLotd의 stubbing 지정 이후 exit을 생성해야 (parkingLot을 생성자 인자로 가지기 때문)

        // when
        Car exitCar = new Car("1111", null);

        // then
        assertThat(exit.pay(exitCar)).isEqualTo(23_000L);

        //결제 후 해당 parkingSpace는 이용가능하고 차가 존재하면 안되니(null) 그것 또한 테스트
        assertThat(parkingSpace.isAvailable()).isTrue();
        assertThat(parkingSpace.getCar()).isNull();
    }

    @DisplayName("주차장에서 나갈때 요금표 대로 결제가 되는지 확인 (5일 6시간 주차 -> 60,000원)")
    @Test
    public void parKingPaymentPayTest7() {
        parkingSpace.setEndParkDateTime(parkingSpace.getStartParkDateTime().plusDays(5).plusHours(6));

        when(parkingLot.findParkingSpaceByNumber(car.getNumber())).thenReturn(parkingSpace);
        exit = new Exit(parkingLot, null); // parkingLotd의 stubbing 지정 이후 exit을 생성해야 (parkingLot을 생성자 인자로 가지기 때문)

        // when
        Car exitCar = new Car("1111", null);

        // then
        assertThat(exit.pay(exitCar)).isEqualTo(60_000L);

        //결제 후 해당 parkingSpace는 이용가능하고 차가 존재하면 안되니(null) 그것 또한 테스트
        assertThat(parkingSpace.isAvailable()).isTrue();
        assertThat(parkingSpace.getCar()).isNull();

    }


    //** 이거 주차를 주차장 꽉 차면 못하게 하는 것도 테스트하자
    // 그리고 특정 코드에 주차를 하는데 거기에 차가 있으면 어떡함 ????
    // code에 car값이 null이면

    @DisplayName("차량 번호로 주차장에 어느곳에 주차 되어있는지 테스트")
    @Test
    public void carNumberCheckInParkingLotTest() {
        String number = "1111";
        Car car = new Car(number, null);
        String code = "A-1";
        boolean avilable = true;
        ParkingSpace parkingSpace = new ParkingSpace(code, avilable, car);

        when(parkingLot.findParkingSpaceByNumber(number)).thenReturn(parkingSpace);

        // when
        String checkedCarNumber = "1111";
        ParkingSpace checkedParkingSpace = parkingLot.findParkingSpaceByNumber(checkedCarNumber);

        //then
        assertThat(checkedParkingSpace).isEqualTo(parkingSpace);
    }

}
