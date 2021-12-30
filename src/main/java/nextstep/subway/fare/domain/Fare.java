package nextstep.subway.fare.domain;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.path.domain.Path;

public class Fare {
    private final Path path;
    private final FareByDistance distanceFare;
    private final DiscountPolicyByAge discountPolicy;

    public Fare(Path path, FareByDistance distanceFare, DiscountPolicyByAge discountPolicy) {
        this.path = path;
        this.distanceFare = distanceFare;
        this.discountPolicy = discountPolicy;
    }

    public static Fare create(final Path path, LoginMember user) {
        return new Fare(path, FareByDistance.of(path.getDistance()), DiscountPolicyByAge.of(user));
    }

    public SubwayFare getFare() {
        SubwayFare subwayFare = distanceFare.getFare(path.getDistance())
                .plus(path.getMaxOverFareOfLine())
                .minus(discountPolicy.discountFare);
        return subwayFare.multiple(discountPolicy.chargeRate);
    }

    public Path getPath() {
        return path;
    }

    public FareByDistance getDistanceFare() {
        return distanceFare;
    }

    public DiscountPolicyByAge getDiscountPolicy() {
        return discountPolicy;
    }
}
