package nextstep.subway.enums;

import nextstep.subway.exception.SubwayPatchException;

import java.math.BigDecimal;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum SubwayFarePolicy {
    ADULT(a -> a >= 19, 0),
    TEENAGER(age -> age >= 13 && age < 19, 0.2),
    CHILD(age -> age >=6 && age < 13, 0.5),
    BABY(age -> age >= 0 && age < 6, 1);


    private static final String NEGATIVE_AGE_ERROR_MESSAGE = "회원 나이가 0보다 작은 값 입니다. 요금 정책을 찾을 수 없습니다. 회원 나이: %s";
    private static final int BASE_FARE = 1250;
    private static final int BASE_DISTANCE = 10;
    private static final int FARE_DISTANCE_BOUNDARY = 50;
    private static final int FARE_DISTANCE_50_OR_LESS = 5;
    private static final int FARE_DISTANCE_50_OVER = 8;
    private static List<SubwayFarePolicy> ENUM_LIST;

    private Predicate<Integer> ageBoundPredicate;
    private double discountRate;

    static {
        ENUM_LIST = Stream.of(SubwayFarePolicy.values()).collect(Collectors.toList());
    }


    SubwayFarePolicy(Predicate<Integer> ageBoundPredicate, double discountRate) {
        this.ageBoundPredicate = ageBoundPredicate;
        this.discountRate = discountRate;
    }



    public static SubwayFarePolicy findFarePolicy(int age) {
        return ENUM_LIST.stream()
                .filter(farePolicy -> farePolicy.ageBoundPredicate.test(age))
                .findFirst()
                .orElseThrow(() -> new SubwayPatchException(NEGATIVE_AGE_ERROR_MESSAGE, age));
    }

    public int calcSubwayFare(int totalDistance, int maxExtraFare) {
        if (totalDistance <= BASE_DISTANCE) {
            return new BigDecimal(1 - discountRate).multiply(new BigDecimal(BASE_FARE + maxExtraFare)).intValue();
        }
        int overFare = calcOverFare(totalDistance);
        int totalSubwayFare = BASE_FARE + overFare + maxExtraFare;
        return new BigDecimal(1 - discountRate).multiply(new BigDecimal(totalSubwayFare)).intValue();
    }

    private int calcOverFare(int distance) {
        if (distance <= FARE_DISTANCE_BOUNDARY) {
            return calcOverFare(distance - BASE_DISTANCE, FARE_DISTANCE_50_OR_LESS);
        }
        int boundaryOverDistance = distance - FARE_DISTANCE_BOUNDARY;
        int i = calcOverFare(FARE_DISTANCE_BOUNDARY - BASE_DISTANCE, FARE_DISTANCE_50_OR_LESS);
        return i + calcOverFare(boundaryOverDistance, FARE_DISTANCE_50_OVER);
    }

    private int calcOverFare(int distance, int fareDistance) {
        return (int) ((Math.ceil((distance - 1) / fareDistance) + 1) * 100);
    }
}
