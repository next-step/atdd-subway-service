package nextstep.subway.path.utils;

import nextstep.subway.line.domain.Line;
import nextstep.subway.path.domain.Path;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class FareCalculator {

    public static final int BASE_FARE = 1_250;
    public static final int THRESHOLD_SECOND = 50;
    public static final int THRESHOLD_FIRST = 10;

    public BigDecimal calculate(List<Line> lines, Path path) {
        int distance = path.getDistance();
        return new BigDecimal(BASE_FARE + calculateOverFare(distance));
    }

    private int calculateOverFare(int distance) {
        if (distance <= THRESHOLD_SECOND) {
            return calculateOverFareLessOrEqualFiftyKilometers(distance - THRESHOLD_FIRST);
        }
        return calculateOverFare(THRESHOLD_SECOND) + calculateOverFareOverFiftyKilometers(distance - THRESHOLD_SECOND);
    }

    private int calculateOverFareOverFiftyKilometers(int overDistance) {
        return (int) ((Math.ceil((overDistance - 1) / 8) + 1) * 100);
    }

    private int calculateOverFareLessOrEqualFiftyKilometers(int overDistance) {
        return (int) ((Math.ceil((overDistance - 1) / 5) + 1) * 100);
    }
}
