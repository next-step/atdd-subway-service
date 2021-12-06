package nextstep.subway.policy.price;

import java.util.function.Function;

import nextstep.subway.line.domain.Distance;
import nextstep.subway.policy.domain.Price;

public class DistancePricePolicy implements PricePolicy {
    Price defaultPrice;
    Distance distance;

    public DistancePricePolicy(Distance distance) {
        super();

        this.defaultPrice = Price.of(1250);

        this.distance = distance;
    }

    @Override
    public Price apply() {
        Price extraFare = calculateExtraFare();

        return defaultPrice.plus(extraFare);
    }

    private Price calculateExtraFare() {
        for (DistanceExtraFareGrade distanceExtraFareGrade : DistanceExtraFareGrade.values()) {
            if (distanceExtraFareGrade.matchDistance(this.distance)) {
                return distanceExtraFareGrade.extraFareStrategy.apply(this.distance);
            }
        }

        return DistanceExtraFareGrade.NONE.extraFareStrategy.apply(this.distance);
    }
}
