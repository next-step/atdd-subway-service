package nextstep.subway.path.domain;

import java.util.Collection;
import java.util.Comparator;

import nextstep.subway.line.domain.Line;

public class FareCalculator {
    private static final String NOT_FOUND_EXTRA_FARE_ERR_MSG = "경로 내의 노선 추가 요금을 찾을 수 없습니다";

    private static final int BASIC_FARE = 1250;
    private static final int EXTRA_FARE_DISTANCE_CRITERIA = 10;
    private static final int EXTRA_FARE_UNIT_DISTANCE = 5;
    private static final int UNIT_EXTRA_FARE = 100;

    private FareCalculator() {
    }

    static int calculateFare(Collection<Line> lines, int distance) {
        final int distanceExtraFare = calculateDistanceExtraFare(distance);
        final int lineExtraFare = calculateLineExtraFare(lines);

        return BASIC_FARE + distanceExtraFare + lineExtraFare;
    }

    private static int calculateDistanceExtraFare(int distance) {
        final int multiplier = calculateExtraFareMultiplier(distance);
        return multiplier * UNIT_EXTRA_FARE;
    }

    private static int calculateLineExtraFare(final Collection<Line> lines) {
        return lines.stream()
            .map(Line::getExtraFare)
            .max(Comparator.naturalOrder())
            .orElseThrow(() -> new RuntimeException(NOT_FOUND_EXTRA_FARE_ERR_MSG));
    }

    private static int calculateExtraFareMultiplier(final int distance) {
        return (int)Math.ceil((double)calculateExtraDistance(distance) / EXTRA_FARE_UNIT_DISTANCE);
    }

    private static int calculateExtraDistance(final int distance) {
        if (distance < EXTRA_FARE_DISTANCE_CRITERIA) {
            return 0;
        }
        return distance - EXTRA_FARE_DISTANCE_CRITERIA;
    }
}
