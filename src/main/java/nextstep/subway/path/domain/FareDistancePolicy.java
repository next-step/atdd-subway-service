package nextstep.subway.path.domain;

import nextstep.subway.common.domain.Fare;
import nextstep.subway.line.domain.Distance;
import org.springframework.util.Assert;

public final class FareDistancePolicy {

    private static final Fare DEFAULT_FARE = Fare.from(1250);
    private static final Fare FARE_ADDITION_STEP = Fare.from(100);
    private static final Distance DEFAULT_FARE_DISTANCE_STANDARD = Distance.from(10);
    private static final Distance SHORT_FARE_ADDITION_DISTANCE_STEP = Distance.from(5);
    private static final Distance FAR_DISTANCE_STANDARD = Distance.from(50);
    private static final Distance FAR_FARE_ADDITION_DISTANCE_STEP = Distance.from(8);

    private final Distance distance;

    private FareDistancePolicy(Distance distance) {
        Assert.notNull(distance, "계산할 거리는 필수입니다.");
        this.distance = distance;
    }

    public static FareDistancePolicy from(Distance distance) {
        return new FareDistancePolicy(distance);
    }

    public Fare fare() {
        if (distance.lessThanOrEqual(DEFAULT_FARE_DISTANCE_STANDARD)) {
            return DEFAULT_FARE;
        }

        if (distance.lessThanOrEqual(FAR_DISTANCE_STANDARD)) {
            return DEFAULT_FARE.sum(shortDistanceFare(distance));
        }

        return DEFAULT_FARE
            .sum(shortDistanceFare(FAR_DISTANCE_STANDARD))
            .sum(farDistanceFare());
    }

    private Fare shortDistanceFare(Distance upToDistance) {
        return farePerDistance(
            upToDistance.subtract(DEFAULT_FARE_DISTANCE_STANDARD),
            SHORT_FARE_ADDITION_DISTANCE_STEP);
    }

    private Fare farDistanceFare() {
        return farePerDistance(
            distance.subtract(FAR_DISTANCE_STANDARD),
            FAR_FARE_ADDITION_DISTANCE_STEP);
    }

    private Fare farePerDistance(Distance distance, Distance step) {
        return FARE_ADDITION_STEP.multiply(distance.ceilDivide(step));
    }

    @Override
    public String toString() {
        return "FareDistancePolicy{" +
            "distance=" + distance +
            '}';
    }
}
