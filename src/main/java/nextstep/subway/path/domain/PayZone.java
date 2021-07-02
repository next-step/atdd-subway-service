package nextstep.subway.path.domain;

import java.util.Arrays;

import nextstep.subway.path.exception.PayZoneNotFoundException;

public enum PayZone {
    FIRST(0, 10, null),
    SECOND(10, 50, 5),
    THIRD(50, Double.MAX_VALUE, 8);

    private static final int NO_POINT = 0;

    double lowerBound;
    double upperBound;
    Integer divider;

    PayZone(double lowerBound, double upperBound, Integer divider) {
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
        this.divider = divider;
    }

    public static PayZone of(double distance) {
        return Arrays.stream(values())
            .filter(z -> z.include(distance))
            .findAny()
            .orElseThrow(() -> new PayZoneNotFoundException("어떤 페이존에도 포함되지 않습니다."));
    }

    private boolean include(double distance) {
        return this.lowerBound < distance && distance <= this.upperBound;
    }

    public int totalPoint(double distance) {
        if (FIRST.equals(this)) {
            return NO_POINT;
        }

        if (SECOND.equals(this)) {
            return getZonePoint(distance, SECOND);
        }

        return getZonePoint(THIRD.lowerBound, SECOND)
            + getZonePoint(distance, THIRD);
    }

    private static int getZonePoint(double distance, PayZone payZone) {
        checkDistancePayZone(distance, payZone);
        return (int) Math.ceil((distance - payZone.lowerBound) / payZone.divider);
    }

    private static void checkDistancePayZone(double distance, PayZone payZone) {
        if (!PayZone.of(distance).equals(payZone)) {
            throw new IllegalArgumentException("거리와 페이존이 일치하지 않습니다.");
        }
    }
}
