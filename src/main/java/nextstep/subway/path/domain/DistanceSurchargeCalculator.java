package nextstep.subway.path.domain;

import java.util.Arrays;

public enum DistanceSurchargeCalculator implements DistanceSurchargeCalculation {

    FIRST(1, 10) {
        @Override
        public Fare calculate(int distance) {
            return Fare.wonOf(0);
        }
    },
    SECOND(11, 50) {
        @Override
        public Fare calculate(int distance) {
            return calculateOverFare(distance - FIRST.end, 5);
        }
    },
    LAST(51, Integer.MAX_VALUE) {
        @Override
        public Fare calculate(int distance) {
            return calculateOverFare(SECOND.end - FIRST.end, 5)
                .plus(calculateOverFare(distance - SECOND.end, 8));
        }
    };

    private final int start;
    private final int end;

    DistanceSurchargeCalculator(int start, int end) {
        this.start = start;
        this.end = end;
    }

    public static DistanceSurchargeCalculator get(int distance) {
        return Arrays.stream(values())
            .filter(r -> distance >= r.start && distance <= r.end)
            .findFirst()
            .orElse(null);
    }

    private static Fare calculateOverFare(int distance, int interval) {
        return Fare.wonOf((((distance - 1) / interval) + 1) * 100);
    }

    public int getStart() {
        return start;
    }

    public int getEnd() {
        return end;
    }
}
