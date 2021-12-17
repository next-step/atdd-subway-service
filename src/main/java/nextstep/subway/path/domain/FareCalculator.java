package nextstep.subway.path.domain;

import java.util.Collection;
import java.util.Comparator;

import nextstep.subway.line.domain.Line;

public class FareCalculator {
    private static final int BASIC_FARE = 1250;
    private static final int EXTRA_FARE_DISTANCE_CRITERIA = 10;
    private static final int EXTRA_FARE_UNIT_DISTANCE = 5;
    private static final int UNIT_EXTRA_FARE = 100;
    private static final int DISCOUNT = 350;
    private static final double CHILDREN_DISCOUNT_RATE = 0.5;
    private static final double TEENAGER_DISCOUNT_RATE = 0.8;
    private static final int BABY_MAX_AGE = 5;
    private static final int CHILDREN_MAX_AGE = 12;
    private static final int TEENAGER_MAX_AGE = 18;

    private FareCalculator() {
    }

    static int calculateFare(Collection<Line> lines, int distance, int memberAge) {
        if (memberAge <= BABY_MAX_AGE) {
            return 0;
        }
        final int distanceExtraFare = calculateDistanceExtraFare(distance);
        final int lineExtraFare = calculateLineExtraFare(lines);
        final int fare = BASIC_FARE + distanceExtraFare + lineExtraFare;

        if (memberAge <= CHILDREN_MAX_AGE) {
            return (int)((fare - DISCOUNT) * CHILDREN_DISCOUNT_RATE);
        }
        if (memberAge <= TEENAGER_MAX_AGE) {
            return (int)((fare - DISCOUNT) * TEENAGER_DISCOUNT_RATE);
        }
        return fare;
    }

    private static int calculateDistanceExtraFare(int distance) {
        final int multiplier = calculateExtraFareMultiplier(distance);
        return multiplier * UNIT_EXTRA_FARE;
    }

    private static int calculateLineExtraFare(final Collection<Line> lines) {
        return lines.stream()
            .map(Line::getExtraFare)
            .max(Comparator.naturalOrder())
            .orElseThrow(() -> new RuntimeException("경로 내의 노선 추가 요금을 찾을 수 없습니다"));
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
