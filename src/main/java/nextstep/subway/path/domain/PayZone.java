package nextstep.subway.path.domain;

import java.util.Arrays;

import nextstep.subway.path.exception.PayZoneFaultException;

public enum PayZone {
    FIRST(10, 50, 5),
    SECOND(50, Double.MAX_VALUE, 8);

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
            .filter(z -> z.includes(distance))
            .findAny()
            .orElseThrow(() -> new PayZoneFaultException("어떤 페이존에도 포함되지 않습니다."));
    }

    public static int totalPoint(double distance) {
        return Arrays.stream(values())
            .map(z -> z.zonePoint(distance))
            .reduce(Integer::sum)
            .orElseThrow(() -> new PayZoneFaultException("포인트 연산에 오류가 있습니다"));
    }

    private int zonePoint(double distance) {
        if (this.isLessThan(distance)) {
            return (int) (this.upperBound - this.lowerBound) / this.divider;
        }

        if (this.includes(distance)) {
            return (int) Math.ceil((distance - this.lowerBound) / this.divider);
        }

        return NO_POINT;
    }

    private boolean isLessThan(double distance) {
        return this.upperBound < distance;
    }

    private boolean includes(double distance) {
        return this.lowerBound < distance && distance <= this.upperBound;
    }
}
