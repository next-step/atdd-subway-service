package nextstep.subway.enums;

import nextstep.subway.exception.SubwayPatchException;

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

    public double getDiscountRate() {
        return discountRate;
    }
}
