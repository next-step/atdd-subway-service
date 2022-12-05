package nextstep.subway.fare.domain;

import nextstep.subway.fare.exception.AgeFareException;

import java.util.Arrays;

import static nextstep.subway.fare.exception.AgeFareExceptionCode.NONE_EXISTS_AGE;

public enum AgeFare {
    CHILD(6, 13, 350, 0.5),
    TEEN(13, 19, 350, 0.2),
    ADULT(19, Integer.MAX_VALUE, 0, 0);

    private int minAge;
    private int maxAge;
    private int deductionFare;
    private double discountRate;

    AgeFare(int minAge, int maxAge, int deductionFare, double discountRate) {
        this.minAge = minAge;
        this.maxAge = maxAge;
        this.deductionFare = deductionFare;
        this.discountRate = discountRate;
    }

    public static AgeFare valueOfRange(int age) {
        return Arrays.stream(AgeFare.values())
                .filter(it -> it.isRange(age))
                .findAny()
                .orElseThrow(() -> new AgeFareException(NONE_EXISTS_AGE));
    }

    private boolean isRange(int age) {
        return this.minAge <= age && age < this.maxAge;
    }


    //요금 입력하면 얼마나오는지
}
