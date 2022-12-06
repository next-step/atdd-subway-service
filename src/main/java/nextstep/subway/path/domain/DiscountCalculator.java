package nextstep.subway.path.domain;

import nextstep.subway.auth.domain.LoginMember;

public class DiscountCalculator {
    private static final Integer DEFAULT_DISCOUNT_FARE = 350;
    private static final double DISCOUNT_RATE_FOR_CHILD = 0.5;
    private static final double DISCOUNT_RATE_FOR_TEEN = 0.8;

    private DiscountCalculator() {

    }

    public static Integer getFare(LoginMember member, Integer fare) {
        if (member == null || member.isAdult()) {
            return fare;
        }
        fare -= DEFAULT_DISCOUNT_FARE;
        if (member.isTeen()) {
            return (int) Math.ceil(fare * DISCOUNT_RATE_FOR_TEEN);
        }
        if (member.isChild()) {
            return (int) Math.ceil(fare * DISCOUNT_RATE_FOR_CHILD);
        }
        return fare;
    }
}
