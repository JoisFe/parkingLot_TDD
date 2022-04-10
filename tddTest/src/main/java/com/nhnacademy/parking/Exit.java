package com.nhnacademy.parking;

import java.time.Duration;
import java.time.LocalDateTime;

public class Exit implements Runnable {
    private static final long TEN_MINUTE_SECOND = 10 * 60;
    private static final long ONE_DAY_SECOND = 24 * 60 * 60;
    private static final long THIRTY_MINUTE_SECOND = 30 * 60;
    private static final long ONE_HOUR_SECOND = 60 * 60;

    private static final double PAYCO_USER_DISCOUNT_RATE = 0.1;

    private static final long TWO_HOUR_SECOND = 2 * 60 * 60;

    private ParkingLot parkingLot;
    private User user;

    public Exit(ParkingLot parkingLot, User user) {
        this.parkingLot = parkingLot;
        this.user = user;
    }

    public void setParkingSpaceEndTime(Car car) {
        ParkingSpace parkingSpace = parkingLot.findParkingSpaceByNumber(car.getNumber());
        parkingSpace.setEndParkDateTime(LocalDateTime.now());
    }

    public long pay(Car car) {
        ParkingSpace parkingSpace = parkingLot.findParkingSpaceByNumber(car.getNumber());

        Long payment = 0L;

        Duration duration = Duration.between(parkingSpace.getStartParkDateTime(), parkingSpace.getEndParkDateTime());

        long secondTime = duration.getSeconds();

        long dayPaymentCnt = secondTime / ONE_DAY_SECOND;
        if (secondTime % ONE_DAY_SECOND == 0) {
            -- dayPaymentCnt;
        }

        payment += 10_000L * dayPaymentCnt;
        secondTime -= ONE_DAY_SECOND * dayPaymentCnt;


        Long plusPayment = 1_000L;
        secondTime -= THIRTY_MINUTE_SECOND;

        if (secondTime > 0) {
            long plusPaymentCnt = secondTime / TEN_MINUTE_SECOND + 1;
            if (secondTime % TEN_MINUTE_SECOND == 0) {
                --plusPaymentCnt;
            }

            plusPayment += 500L * plusPaymentCnt;
        }

        if (plusPayment > 10_000L) {
            plusPayment = 10_000L;
        }

        payment += plusPayment;

        parkingSpace.setStartParkDateTime(null);
        parkingSpace.setEndParkDateTime(null);
        parkingSpace.openAvailable();
        parkingSpace.exitCar();

        return payment;
    }

    public long newPaymentPolicyPay(User user) {
        Car car = user.getCar();
        ParkingSpace parkingSpace = parkingLot.findParkingSpaceByNumber(car.getNumber());

        Long payment = 0L;

        Duration duration = Duration.between(parkingSpace.getStartParkDateTime(), parkingSpace.getEndParkDateTime());

        long secondTime = duration.getSeconds();

        secondTime = useParkingTicket(secondTime);

        long dayPaymentCnt = secondTime / ONE_DAY_SECOND;
        if (secondTime % ONE_DAY_SECOND == 0) {
            -- dayPaymentCnt;
        }

        payment += 15_000L * dayPaymentCnt;
        secondTime -= ONE_DAY_SECOND * dayPaymentCnt;


        Long plusPayment = 0L;
        if (secondTime <= THIRTY_MINUTE_SECOND) {
            secondTime = 0;
        }

        if (secondTime > 0) {
            plusPayment += 1000;
            secondTime -= ONE_HOUR_SECOND;
        }


        if (secondTime > 0) {
            long plusPaymentCnt = secondTime / TEN_MINUTE_SECOND + 1;
            if (secondTime % TEN_MINUTE_SECOND == 0) {
                --plusPaymentCnt;
            }

            plusPayment += 500L * plusPaymentCnt;
        }

        if (plusPayment > 15_000L) {
            plusPayment = 15_000L;
        }

        payment += plusPayment;

        parkingSpace.setStartParkDateTime(null);
        parkingSpace.setEndParkDateTime(null);
        parkingSpace.openAvailable();
        parkingSpace.exitCar();

        // 경차 시 반값
        if (car.getCarType().equals(CarType.LIGHT_CAR)) {
            payment /= 2;
        }

        // 페이코 회원시 10% 할인
        if (parkingLot.findUserByCar(car).isPaycoUser()) {
            payment = (long)(payment * (1 - PAYCO_USER_DISCOUNT_RATE));
        }

        return payment;
    }

    // 쿠폰 사용 정책
    /*
    무조건 결제시 사용
    단 두가지 모두 존재시 2시간 미만 -> 1시간 쿠폰 먼저 사용
    아니면 -> 2시간 쿠폰 먼저 사용
     */
    private long useParkingTicket(long secondTime) {
        if (user.getParkingTicket().getTwoHourParkingTicket() > 0 && secondTime < TWO_HOUR_SECOND) {
            if (user.getParkingTicket().getOneHourParkingTicket() > 0) {
                secondTime -= ONE_HOUR_SECOND;
                user.getParkingTicket().useOneHourParkingTicket();
            } else {
                secondTime -= TWO_HOUR_SECOND;
                user.getParkingTicket().useTwoHourParkingTicket();
            }
        } else if (user.getParkingTicket().getTwoHourParkingTicket() > 0) {
            secondTime -= TWO_HOUR_SECOND;
            user.getParkingTicket().useTwoHourParkingTicket();
        } else if (user.getParkingTicket().getOneHourParkingTicket() > 0) {
            secondTime -= ONE_HOUR_SECOND;
            user.getParkingTicket().useOneHourParkingTicket();
        }

        if (secondTime < 0) secondTime = 0;

        return secondTime;
    }

    public User findUserByCar(Car car) {
        return parkingLot.findUserByCar(car);
    }

    @Override
    public void run() {
        try {
            Thread.sleep(1000);
            System.out.println("exit 실행중 (1초 지연 시킴)");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
