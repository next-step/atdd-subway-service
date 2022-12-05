package nextstep.subway.path.domain;

import nextstep.subway.auth.domain.LoginMember;

public class DiscountCalculator {
    private static final Integer DISCOUNT_DEFAULT = 350;
    private static final double DISCOUNT_CHILD = 0.5;
    private static final double DISCOUNT_TEEN = 0.8;

    private DiscountCalculator() {

    }

    public static Integer getFare(LoginMember member, Integer fare) {
        if (member == null || member.isAdult()) {
            return fare;
        }
        fare -= DISCOUNT_DEFAULT;
        if (member.isTeen()) {
            return (int) Math.ceil(fare * DISCOUNT_TEEN);
        }
        if (member.isChild()) {
            return (int) Math.ceil(fare * DISCOUNT_CHILD);
        }
        return fare;
    }
}
