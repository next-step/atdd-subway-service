package nextstep.subway.path.domain;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.line.domain.Fare;

import java.math.BigDecimal;

public class PathFareAge {
    private static final BigDecimal FARE_BASE = new BigDecimal("1250");

    public static Fare of(final LoginMember loginMember) {
        return calculatorFare(loginMember, Fare.from(FARE_BASE));
    }

    private static Fare calculatorFare(final LoginMember loginMember, final Fare fare) {
        if (loginMember.isGuest()) {
            return fare;
        }
        return AgeFarePolicy.of(loginMember, fare);
    }

    private PathFareAge() {
    }
}
