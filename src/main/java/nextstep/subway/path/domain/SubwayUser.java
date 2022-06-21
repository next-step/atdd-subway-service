package nextstep.subway.path.domain;

import nextstep.subway.auth.domain.LoginMember;

public class SubwayUser {

    public static final int DEFAULT_SUBTRACT_AMOUNT_AT_AGE_POLICY = 350;
    public static final int CHILD_DISCOUNT_RATE = 50;
    public static final int TEEN_DISCOUNT_RATE = 20;
    private static final int CHILD_INTERVAL_START_AGE = 6;
    private static final int CHILD_INTERVAL_END_AGE = 13;
    private static final int TEEN_INTERVAL_START_AGE = 13;
    private static final int TEEN_INTERVAL_END_AGE = 19;
    private final Integer age;
    private final boolean isAnonymous;

    public SubwayUser(Integer age, boolean isAnonymous) {
        this.age = age;
        this.isAnonymous = isAnonymous;
    }

    public static SubwayUser of(LoginMember loginMember) {
        if (loginMember.getId() == null) {
            return new SubwayUser(0, true);
        }
        return new SubwayUser(loginMember.getAge(), false);
    }

    public boolean isAnonymous() {
        return isAnonymous;
    }

    public SubwayFare discountFareByAge(SubwayFare beforeDiscountFare) {
        if (isChild()) {
            return beforeDiscountFare.subtract(DEFAULT_SUBTRACT_AMOUNT_AT_AGE_POLICY)
                    .discountedByPercent(CHILD_DISCOUNT_RATE);
        }
        if (isTeen()) {
            return beforeDiscountFare.subtract(DEFAULT_SUBTRACT_AMOUNT_AT_AGE_POLICY)
                    .discountedByPercent(TEEN_DISCOUNT_RATE);
        }
        return beforeDiscountFare;
    }

    private boolean isChild() {
        return age >= CHILD_INTERVAL_START_AGE && age < CHILD_INTERVAL_END_AGE;
    }

    private boolean isTeen() {
        return age >= TEEN_INTERVAL_START_AGE && age < TEEN_INTERVAL_END_AGE;
    }
}
