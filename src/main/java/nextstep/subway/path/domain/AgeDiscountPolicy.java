package nextstep.subway.path.domain;

import java.util.Arrays;

public enum AgeDiscountPolicy {
    GENERAL(0, 0, 0, 1.0), 
    CHILD(6, 12, 350, 0.5), 
    YOUTH(13, 18, 350, 0.8);
    
    private final int ageRangeMin;
    private final int ageRangeMax;
    private final int discountFare;
    private final double discountPercent;

    AgeDiscountPolicy(int ageRangeMin, int ageRangeMax, int discountFare, double discountPercent) {
        this.ageRangeMin = ageRangeMin;
        this.ageRangeMax = ageRangeMax;
        this.discountFare = discountFare;
        this.discountPercent = discountPercent;
    }
    
    public static int discount(int fare, int age) {
        AgeDiscountPolicy policy = judgePolicyGroup(age);
        return (int) ((fare - policy.discountFare) * policy.discountPercent);
    }
    
    private static AgeDiscountPolicy judgePolicyGroup(int age) {
        return Arrays.stream(values())
                .filter(policy -> policy.judgeAgeRange(age))
                .findFirst()
                .orElse(GENERAL);
    }
    
    private boolean judgeAgeRange(int age) {
        return age >= this.ageRangeMin && age <= this.ageRangeMax;
    }

}
