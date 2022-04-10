package com.nhnacademy.parking;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

// 입구 n = 3 이라 가정
public class SimultaneousParkInTest {
    ParkingLot parkingLot;

    Runnable entrance1, entrance2, entrance3, entrance4;
    Runnable exit1, exit2, exit3, exit4;
    Car car1, car2, car3, car4;
    User user1, user2, user3, user4;

    @BeforeEach
    void setUp() {
        parkingLot = mock(ParkingLot.class);

        entrance1 = (Runnable) new Entrance(parkingLot);
        entrance2 = (Runnable) new Entrance(parkingLot);
        entrance3 = (Runnable) new Entrance(parkingLot);
        entrance4 = (Runnable) new Entrance(parkingLot);

        Money money = new Money(10000);

        car1 = new Car("1111", CarType.REGULAR_CAR);
        car2 = new Car("2222", CarType.REGULAR_CAR);
        car3 = new Car("3333", CarType.REGULAR_CAR);
        car4 = new Car("4444", CarType.REGULAR_CAR);


        user1 = new User("jo", money, car1, false, new ParkingTicket(0, 0));
        user2 = new User("lee", money, car2, false, new ParkingTicket(0, 0));
        user3 = new User("kim", money, car3, false, new ParkingTicket(0, 0));
        user4 = new User("park", money, car4, false, new ParkingTicket(0, 0));

        exit1 = (Runnable) new Exit(parkingLot, user1);
        exit2 = (Runnable) new Exit(parkingLot, user2);
        exit3 = (Runnable) new Exit(parkingLot, user3);
        exit4 = (Runnable) new Exit(parkingLot, user4);
    }

    @DisplayName("한번에 최대 n개의 차만 Entrance에서 scan이 가능 (n == 3라 가정) -> " +
        "각 쓰레드마다 1초씩 지연 걸었으니 threadPool 제한이 없었다면 4개 쓰레드가 존재할 것 (메인, 테스트 쓰레드 제외)" +
        "-> 하지만 threadPool로 인해 3개만 제한")
    @Test
    void parkInSimultaneousThreeExcessTest() throws InterruptedException {
        int maxThreadCnt = 3;

        ExecutorService executorService = Executors.newFixedThreadPool(maxThreadCnt);

        // when
        executorService.submit(entrance1);
        executorService.submit(entrance2);
        executorService.submit(entrance3);
        executorService.submit(entrance4);


        // then
        // 메인과 테스트 쓰레드 2개 제거
        assertThat(Thread.activeCount() - 2).isEqualTo(3);

        executorService.shutdown();
    }

    @DisplayName("한번에 최대 n개의 차만 Exit에서 나가기 가능 (n == 3라 가정) -> " +
        "각 쓰레드마다 1초씩 지연 걸었으니 threadPool 제한이 없었다면 4개 쓰레드가 존재할 것 (메인, 테스트 쓰레드 제외)" +
        "-> 하지만 threadPool로 인해 3개만 제한")
    @Test
    void parkOutSimultaneousThreeExcessTest() throws InterruptedException {
        int maxThreadCnt = 3;

        ExecutorService executorService = Executors.newFixedThreadPool(maxThreadCnt);

        // when
        executorService.submit(exit1);
        executorService.submit(exit2);
        executorService.submit(exit3);
        executorService.submit(exit4);

        // then
        // 메인과 테스트 쓰레드 그리고 위 parkInSimultaneousThreeExcessTest()에서 실행중인 3개의 쓰레드 까지 제거한 상태
        assertThat(Thread.activeCount() - 2 - 3).isEqualTo(3);
        executorService.shutdown();
    }
}
