package nextstep.subway.path.utils;

import static java.util.Comparator.*;

import nextstep.subway.line.domain.Line;
import nextstep.subway.path.domain.Fare;
import nextstep.subway.path.domain.Path;
import nextstep.subway.path.domain.PayZone;

public class FareCalculator {

    public static final int BASIC_FARE = 1250;
    private static final int DISTANCE_FARE = 100;

    private FareCalculator() {
    }

    public static Fare calculate(Path path) {
        return new Fare(BASIC_FARE
            + calculateOverFare(path.distance())
            + getLineAdditionalFare(path));
    }

    private static int getLineAdditionalFare(Path path) {
        return path.getPassingLines()
            .stream()
            .map(Line::additionalFare)
            .max(naturalOrder())
            .orElse(0);
    }

    private static int calculateOverFare(double distance) {
        PayZone payZone = PayZone.of(distance);
        return payZone.totalPoint(distance) * DISTANCE_FARE;
    }
}
