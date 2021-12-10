package nextstep.subway.line.domain;

import static java.util.Arrays.*;
import static nextstep.subway.line.domain.Fare.NOT_BENEFIT;
import static nextstep.subway.line.domain.FareRuleDistance.DistanceConstant.EIGHT;
import static nextstep.subway.line.domain.FareRuleDistance.DistanceConstant.FIFTY;
import static nextstep.subway.line.domain.FareRuleDistance.DistanceConstant.FIVE;
import static nextstep.subway.line.domain.FareRuleDistance.DistanceConstant.ONE;
import static nextstep.subway.line.domain.FareRuleDistance.DistanceConstant.ONE_HUNDRED_PERCENT;
import static nextstep.subway.line.domain.FareRuleDistance.DistanceConstant.TEN;

import java.util.Arrays;
import java.util.function.Function;

public enum FareRuleDistance {
    DEFAULT(new Distance(0), new Distance(10), s -> NOT_BENEFIT),
    FIVE_KM(new Distance(TEN), new Distance(FIFTY),
        distance -> Fare.ceil(distance.subtract(new Distance(ONE))
                                      .divide(FIVE))
                                    .add(ONE)
                                    .multiply(ONE_HUNDRED_PERCENT)),

    EIGHT_KM(new Distance(FIFTY), new Distance(Double.MAX_VALUE),
        distance -> Fare.ceil(distance.subtract(new Distance(ONE))
                                      .divide(EIGHT))
                                      .add(ONE)
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

    protected static class DistanceConstant {
        public static final int ONE = 1;
        public static final int FIVE = 5;
        public static final int EIGHT = 8;
        public static final double TEN = 10;
        public static final double FIFTY = 50;
        public static final int ONE_HUNDRED_PERCENT = 100;
    }
}
