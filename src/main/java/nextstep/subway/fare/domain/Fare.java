package nextstep.subway.fare.domain;

import nextstep.subway.fare.policy.DiscountPolicy;
import nextstep.subway.path.domain.Path;

public class Fare {
    private final int fare;
    private Fare(int fare) {
        this.fare = fare;
    }

    public static Fare of(Path path, DiscountPolicy discountPolicy) {
        return new Fare(finalFare(path, discountPolicy));
    }

    private static int finalFare(Path path, DiscountPolicy discountPolicy) {
        int calculateFare = FareType.BASIC.getFare() + DistanceType.distanceExtraFare(path.getDistance())
                + path.findExtraFare();
        return discountPolicy.discount(calculateFare);
    }

    public int get() {
        return this.fare;
    }
}
