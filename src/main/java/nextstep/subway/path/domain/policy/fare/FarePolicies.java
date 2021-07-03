package nextstep.subway.path.domain.policy.fare;

import nextstep.subway.path.domain.policy.fare.discount.DiscountByAgeStrategy;
import nextstep.subway.path.domain.policy.fare.distance.OverFareByDistanceStrategy;
import nextstep.subway.path.domain.policy.fare.line.OverFareByLineStrategy;

import java.util.Optional;

public class FarePolicies {
    private OverFareByLineStrategy lineStrategy;
    private OverFareByDistanceStrategy distanceStrategy;
    private DiscountByAgeStrategy discountByAgeStrategy;

    public FarePolicies() {}

    public FarePolicies(OverFareByLineStrategy lineStrategy, OverFareByDistanceStrategy distanceStrategy, DiscountByAgeStrategy discountByAgeStrategy) {
        this.lineStrategy = lineStrategy;
        this.distanceStrategy = distanceStrategy;
        this.discountByAgeStrategy = discountByAgeStrategy;
    }

    public Optional<OverFareByLineStrategy> getLineStrategy() {
        return Optional.ofNullable(lineStrategy);
    }

    public Optional<OverFareByDistanceStrategy> getDistanceStrategy() {
        return Optional.ofNullable(distanceStrategy);
    }

    public Optional<DiscountByAgeStrategy> getDiscountByAgeStrategy() {
        return Optional.ofNullable(discountByAgeStrategy);
    }

    public void changeLineStrategy(OverFareByLineStrategy lineStrategy) {
        this.lineStrategy = lineStrategy;
    }

    public void changeDistanceStrategy(OverFareByDistanceStrategy distanceStrategy) {
        this.distanceStrategy = distanceStrategy;
    }

    public void changeDiscountByAgeStrategy(DiscountByAgeStrategy discountByAgeStrategy) {
        this.discountByAgeStrategy = discountByAgeStrategy;
    }
}
