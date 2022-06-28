package nextstep.subway.fare.domain;

import nextstep.subway.auth.domain.LoginMember;

public class Fare {
    private static final int DEFAULT_FARE = 1_250;

    private final int value;

    private Fare(int value) {
        this.value = value;
    }

    public static Fare of(final LoginMember loginMember, final int extraCharge, final int distance) {
        final int distanceExtraCharge = DistanceFarePolicy.ofExtraCharge(distance);
        final int totalCharge = DEFAULT_FARE + extraCharge + distanceExtraCharge;
        if (isNonMember(loginMember)) {
            return new Fare(totalCharge);
        }
        return new Fare(AgeFarePolicy.ofDiscount(loginMember.getAge(), totalCharge));
    }

    private static boolean isNonMember(final LoginMember loginMember) {
        return loginMember.isNonMember();
    }

    public int getValue() {
        return value;
    }
}
