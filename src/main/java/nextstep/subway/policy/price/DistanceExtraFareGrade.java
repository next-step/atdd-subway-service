package nextstep.subway.policy.price;

import java.util.function.Function;

import nextstep.subway.line.domain.Distance;
import nextstep.subway.policy.domain.Price;

public enum DistanceExtraFareGrade {
    NONE(1, 10,  distance -> Price.of(0)),
    ONE_LEVEL(11, 50, distance -> Price.of(Constants.EXTRA_FARE_PER_DISTANCE * (int)((distance.value() - Constants.NONE_EXTRA_FARE_THRESHOLD_DISTANCE) / Constants.ONE_LEVEL_EXTRA_FARE_UNIT_DISTANCE))),
    TWO_LEVEL(51, Integer.MAX_VALUE, distance -> Price.of(Constants.EXTRA_FARE_PER_DISTANCE * (int)((distance.value() - Constants.ONE_LEVEL_EXTRA_FARE_THRESHOLD_DISTANCE ) / Constants.TWO_LEVEL_EXTRA_FARE_UNIT_DISTANCE) + Constants.ONE_LEVEL_TOTAL_EXTRA_FARE));

    public int startDistance;
    public int endDistance;
    public Function<Distance, Price> extraFareStrategy;

    DistanceExtraFareGrade(int startDistance, int endDistance, Function<Distance, Price> extraFareStrategy) {
        this.startDistance = startDistance;
        this.endDistance = endDistance;
        this.extraFareStrategy = extraFareStrategy;
    }

    private class Constants {
        public static final int ONE_LEVEL_TOTAL_EXTRA_FARE = 800;
        public static final int NONE_EXTRA_FARE_THRESHOLD_DISTANCE = 10;
        public static final int ONE_LEVEL_EXTRA_FARE_THRESHOLD_DISTANCE = 50;
        public static final int ONE_LEVEL_EXTRA_FARE_UNIT_DISTANCE = 5;
        public static final int TWO_LEVEL_EXTRA_FARE_UNIT_DISTANCE = 8;
        public static final int EXTRA_FARE_PER_DISTANCE = 100;
    }

    public boolean matchDistance(Distance distance) {
        return distance.value() > startDistance && distance.value() <= endDistance;
    }
}
