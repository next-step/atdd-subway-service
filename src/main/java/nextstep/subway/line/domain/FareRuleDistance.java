package nextstep.subway.line.domain;

import static java.util.Arrays.stream;
import static nextstep.subway.line.domain.Fare.NOT_BENEFIT;
;

import static nextstep.subway.line.domain.FareRuleDistance.FareRuleDistanceConstant.DISTANCE_ONE;
import static nextstep.subway.line.domain.FareRuleDistance.FareRuleDistanceConstant.FARE_ONE;
import static nextstep.subway.line.domain.FareRuleDistance.FareRuleDistanceConstant.FIFTY;
import static nextstep.subway.line.domain.FareRuleDistance.FareRuleDistanceConstant.MAX_TEN;
import static nextstep.subway.line.domain.FareRuleDistance.FareRuleDistanceConstant.MIN_TEN;
import static nextstep.subway.line.domain.FareRuleDistance.FareRuleDistanceConstant.MIN_ZERO;
import static nextstep.subway.line.domain.FareRuleDistance.FareRuleDistanceConstant.ONE_HUNDRED_PERCENT;
import static nextstep.subway.line.domain.FareRuleDistance.FareRuleDistanceConstant.PER_DISTANCE_EIGHT;
import static nextstep.subway.line.domain.FareRuleDistance.FareRuleDistanceConstant.PER_DISTANCE_FIVE;

import java.util.function.Function;



public enum FareRuleDistance {
    /**
     * 
     * 실제이동걸리 = 기본거리를 제외한 거리 
     * ((Fare.ceil((실제이동거리 - 1) / 몇키로마다) + 1) * 100);
     */
    DEFAULT(MIN_ZERO, MAX_TEN, s -> NOT_BENEFIT),
    FIVE_KM(MIN_TEN, FIFTY,
        distance -> Fare.ceil(distance.subtract(DISTANCE_ONE)
                                      .divide(PER_DISTANCE_FIVE))
                                    .add(FARE_ONE)
                                    .multiply(ONE_HUNDRED_PERCENT)),

    EIGHT_KM(FIFTY, FareRuleDistanceConstant.MAX_VALUE,
        distance -> Fare.ceil(distance.subtract(DISTANCE_ONE)
                                      .divide(PER_DISTANCE_EIGHT))
                                      .add(FARE_ONE)
                                      .multiply(ONE_HUNDRED_PERCENT));

    private final Distance start;
    private final Distance end;
    private Function<Distance, Fare> expression;

    FareRuleDistance(Distance start, Distance end, Function<Distance, Fare> expression) {
        this.start = start;
        this.end = end;
        this.expression = expression;
    }

    public static Fare calculate(Distance distance) {
        FareRuleDistance findRule = findRuleByDistance(distance);
        Distance leftDistance = leftDistance(distance, findRule);
        return findRule.expression.apply(leftDistance);
    }

    private static FareRuleDistance findRuleByDistance(Distance distance) {
        return stream(FareRuleDistance.values())
                                      .filter(d -> d.between(distance))
                                      .findFirst()
                                      .orElse(DEFAULT);
    }

    private static Distance leftDistance(Distance distance, FareRuleDistance findDistanceRule) {
        return distance.subtract(findDistanceRule.start);
    }

    private boolean between(Distance distance) {
        return start.greaterThan(distance) && end.lessThenOrEquals(distance);
    }

    protected static class FareRuleDistanceConstant {

        public static final Distance PER_DISTANCE_FIVE = new Distance(5);
        public static final Distance PER_DISTANCE_EIGHT = new Distance(8);

        public static final Distance MIN_ZERO = new Distance(0);
        public static final Distance MAX_TEN = new Distance(10);
        public static final Distance MIN_TEN = new Distance(10);
        public static final Distance FIFTY = new Distance(50);
        public static final Distance MAX_VALUE = new Distance(Integer.MAX_VALUE);

        public static final Fare ONE_HUNDRED_PERCENT = new Fare(100);
        public static final Fare FARE_ONE = new Fare(1L);

        public static final Distance DISTANCE_ONE = new Distance(1);
    }
}
