package nextstep.subway.policy.price;

import nextstep.subway.line.domain.Distance;
import nextstep.subway.policy.domain.Price;

public class DistancePricePolicy implements PricePolicy {
    private static final int ONE_LEVEL_TOTAL_EXTRA_FARE = 800;
    private static final int NONE_EXTRA_FARE_THRESHOLD_DISTANCE = 10;
    private static final int ONE_LEVEL_EXTRA_FARE_THRESHOLD_DISTANCE = 50;
    private static final int ONE_LEVEL_EXTRA_FARE_UNIT_DISTANCE = 5;
    private static final int TWO_LEVEL_EXTRA_FARE_UNIT_DISTANCE = 8;
    private static final int EXTRA_FARE_PER_DISTANCE = 100;

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
        if (this.distance.value() > NONE_EXTRA_FARE_THRESHOLD_DISTANCE 
            && this.distance.value() <= ONE_LEVEL_EXTRA_FARE_THRESHOLD_DISTANCE) {
            int extraFare = EXTRA_FARE_PER_DISTANCE * (int)((this.distance.value() - NONE_EXTRA_FARE_THRESHOLD_DISTANCE) / ONE_LEVEL_EXTRA_FARE_UNIT_DISTANCE);
            return Price.of(extraFare);
        }

        if (this.distance.value() > ONE_LEVEL_EXTRA_FARE_THRESHOLD_DISTANCE) {
            int extraFare = EXTRA_FARE_PER_DISTANCE * (int)((this.distance.value() - ONE_LEVEL_EXTRA_FARE_THRESHOLD_DISTANCE ) / TWO_LEVEL_EXTRA_FARE_UNIT_DISTANCE)  + ONE_LEVEL_TOTAL_EXTRA_FARE;
            return Price.of(extraFare);
        }

        return Price.of(0);
    }
}
