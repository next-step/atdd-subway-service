package nextstep.subway.path.domain;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.line.domain.Distance;

public class Fare {
    private final int fare;

    public Fare(int fare) {
        this.fare = fare;
    }

    public static Fare of(Distance distance, int maxLineFare, LoginMember loginMember) {
        int fare = FareCalculator.calculateFare(distance) + maxLineFare;
        return new Fare(DiscountCalculator.getFare(loginMember, fare));
    }

    public int getFare() {
        return fare;
    }
}
