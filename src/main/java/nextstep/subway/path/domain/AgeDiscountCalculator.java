package nextstep.subway.path.domain;

import java.util.Arrays;

public class AgeDiscountCalculator {
    public static int discount(int fare, int age) {
        AgeDiscountPolicy policy = judgePolicyGroup(age);
        return (int) ((fare - policy.discountFare) * policy.discountPercent);
    }
    
    private static AgeDiscountPolicy judgePolicyGroup(int age) {
        return Arrays.stream(AgeDiscountPolicy.values())
                .filter(policy -> judgeAgeRange(policy, age))
                .findFirst()
                .orElse(AgeDiscountPolicy.GENERAL);
    }
    
    private static boolean judgeAgeRange(AgeDiscountPolicy policy, int age) {
        return age >= policy.ageRangeMin && age <= policy.ageRangeMax;
    }

}
