package nextstep.subway.fare;

import nextstep.subway.member.domain.Member;

import java.util.Arrays;

public enum DiscountAgeFarePolicy {
    CHILD(6, 13, 350, 0.5),
    TEENAGER(13, 19, 350, 0.8);

    private int moreAge;
    private int lessAge;
    private int deduction;
    private double calculationRate;

    DiscountAgeFarePolicy(int moreAge, int lessAge, int deduction, double calculationRate) {
        this.moreAge = moreAge;
        this.lessAge = lessAge;
        this.deduction = deduction;
        this.calculationRate = calculationRate;
    }

    public static double calculateDiscountFare(int memberAge, int fare) {
        return Arrays.stream(DiscountAgeFarePolicy.values())
                .filter(discountAgeFarePolicy -> discountAgeFarePolicy.isBetweenAge(memberAge))
                .findAny()
                .map(discountAgeFarePolicy -> discountAgeFarePolicy.calculateFare(fare))
                .orElse(Double.valueOf(fare));
    }

    private double calculateFare(int fare) {
        return (fare - deduction) * calculationRate;
    }

    private boolean isBetweenAge(int age) {
        return moreAge <= age && age < lessAge;
    }
}
