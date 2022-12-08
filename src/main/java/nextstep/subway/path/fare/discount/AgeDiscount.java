package nextstep.subway.path.fare.discount;

import java.util.Arrays;
import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.path.fare.Fare;

public enum AgeDiscount {
    ANONYMOUS(Integer.MAX_VALUE, Integer.MAX_VALUE, fare -> fare),
    CHILDREN(Constants.MIN_CHILDREN_AGE,
             Constants.MAX_CHILDREN_AGE,
             fare -> fare.minus(Constants.BASIC_DISCOUNT_FARE).multiply(Constants.CHILDREN_DISCOUNT_PERCENT)),
    TEENAGER(Constants.MIN_TEEN_AGE,
             Constants.MAX_TEEN_AGE,
             fare -> fare.minus(Constants.BASIC_DISCOUNT_FARE).multiply(Constants.TEENAGER_DISCOUNT_PERCENT)),
    ADULT(18,
          Integer.MAX_VALUE,
          fare -> fare);

    private final int minAge;
    private final int maxAge;
    private final AgeDiscountStrategy ageDiscountStrategy;

    AgeDiscount(int minAge, int maxAge, AgeDiscountStrategy ageDiscountStrategy) {
        this.minAge = minAge;
        this.maxAge = maxAge;
        this.ageDiscountStrategy = ageDiscountStrategy;
    }

    public static Fare discountFare(LoginMember loginMember, Fare fare) {
        AgeDiscount ageDiscount = findAgeDiscountByAge(loginMember.getAge());
        return ageDiscount.ageDiscountStrategy.discount(fare);
    }

    public static AgeDiscount findAgeDiscountByAge(int age) {
        return Arrays.stream(AgeDiscount.values())
                     .filter(ageDiscount -> ageDiscount.isWithinRange(age))
                     .findAny()
                     .orElse(ANONYMOUS);
    }

    private boolean isWithinRange(int age) {
        return minAge <= age && age <= maxAge;
    }

    private static class Constants {
        public static final Fare BASIC_DISCOUNT_FARE = Fare.valueOf(350);
        public static final double CHILDREN_DISCOUNT_PERCENT = 0.5;
        public static final double TEENAGER_DISCOUNT_PERCENT = 0.8;
        public static final int MIN_CHILDREN_AGE = 6;
        public static final int MAX_CHILDREN_AGE = 12;
        public static final int MIN_TEEN_AGE = 13;
        public static final int MAX_TEEN_AGE = 18;
    }
}
