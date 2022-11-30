package nextstep.subway.fare.domain;

import nextstep.subway.exception.NotFoundAgeFarePolicy;
import nextstep.subway.message.ExceptionMessage;

import java.util.Arrays;

public enum AgeFarePolicy {
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
                .orElseThrow(() -> new NotFoundAgeFarePolicy(ExceptionMessage.AGE_FARE_POLICY_NOT_EXIST));
    }

    private boolean ranged(int age) {
        return this.minAge <= age && age < this.maxAge;
    }

    public int discount(int fare) {
        return (int) ((fare - this.deductionFare) * (1 - this.discountRate));
    }
}
