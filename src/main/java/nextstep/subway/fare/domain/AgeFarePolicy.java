package nextstep.subway.fare.domain;


import nextstep.subway.exception.BadRequestException;

import java.util.Arrays;

import static nextstep.subway.utils.Message.AGE_FARE_POLICY_NOT_EXIST;
import static nextstep.subway.utils.Message.FARE_LESS_THAN_DEDUCTION_FARE;

public enum AgeFarePolicy {
    ADULT(19, Integer.MAX_VALUE, 0, 0),
    TEENAGER(13, 19, 350, 0.2),
    CHILD(6, 13, 350, 0.5);

    private int minAge;
    private int maxAge;
    private int deductionFare;
    private double discountRate;

    AgeFarePolicy(int minAge, int maxAge, int deductionFare, double discountRate) {
        this.minAge = minAge;
        this.maxAge = maxAge;
        this.deductionFare = deductionFare;
        this.discountRate = discountRate;
    }

    public static AgeFarePolicy findByAge(int age) {
        return Arrays.stream(AgeFarePolicy.values())
                .filter(it -> it.ranged(age))
                .findAny()
                .orElseThrow(() -> new BadRequestException(AGE_FARE_POLICY_NOT_EXIST));
    }

    private boolean ranged(int age) {
        return this.minAge <= age && age < this.maxAge;
    }

    public int discount(int fare) {
        checkFareIsNotLessThanDeductionFare(fare);
        return (int) ((fare - this.deductionFare) * (1 - this.discountRate));
    }

    private void checkFareIsNotLessThanDeductionFare(int fare) {
        if (fare < this.deductionFare) {
            throw new BadRequestException(FARE_LESS_THAN_DEDUCTION_FARE);
        }
    }
}
