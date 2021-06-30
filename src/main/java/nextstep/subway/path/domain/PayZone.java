package nextstep.subway.path.domain;

public enum PayZone {
    FIRST(0, null),
    SECOND(10, 5),
    THIRD(50, 8);

    private static final int NO_POINT = 0;

    double lowerBound;
    Integer divider;

    PayZone(double lowerBound, Integer divider) {
        this.lowerBound = lowerBound;
        this.divider = divider;
    }

    public static PayZone of(double distance) {
        if (isFirstZone(distance)) {
            return FIRST;
        }

        if (isSecondZone(distance)) {
            return SECOND;
        }

        return THIRD;
    }

    private static boolean isFirstZone(double distance) {
        return distance <= SECOND.lowerBound;
    }

    private static boolean isSecondZone(double distance) {
        return SECOND.lowerBound < distance && distance <= THIRD.lowerBound;
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
