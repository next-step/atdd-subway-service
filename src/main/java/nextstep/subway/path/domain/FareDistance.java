package nextstep.subway.path.domain;

import java.util.Arrays;

public enum FareDistance {
    BASIC(0, 10, 0),
    MIDDLE(11, 50, 5),
    LONG(51, 178, 8);

    private static final int ZERO = 0;
    private static final int BASIC_PRICE = 1_250;
    private static final int INCREMENT_FARE = 100;

    private final int start;
    private final int end;
    private final int condition;

    FareDistance(int start, int end, int condition) {
        this.start = start;
        this.end = end;
        this.condition = condition;
    }

    public static int calculate(int distance) {
        FareDistance fareDistance = find(distance);
        if (fareDistance.equals(BASIC)) {
            return BASIC_PRICE;
        }

        if (fareDistance.equals(MIDDLE)) {
            return BASIC_PRICE + fareDistance.calculateFare(distance - MIDDLE.start + 1, MIDDLE.condition);
        }

        return BASIC_PRICE
                + fareDistance.calculateFare(MIDDLE.end - MIDDLE.start + 1, MIDDLE.condition)
                + fareDistance.calculateFare(distance - LONG.start + 1, LONG.condition);
    }

    private static FareDistance find(int distance) {
        return Arrays.stream(FareDistance.values())
                .filter(fareDistance -> fareDistance.isBetween(distance))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("거리별 요금 정책에 부합하지 않은 값입니다."));
    }

    private boolean isBetween(int distance) {
        return start <= distance && distance <= end;
    }

    private int calculateFare(int distance, int condition) {
        if (distance <= ZERO) {
            return ZERO;
        }

        return (int) ((Math.ceil((distance - 1) / condition) + 1) * INCREMENT_FARE);
    }
}
