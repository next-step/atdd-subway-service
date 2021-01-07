package nextstep.subway.fare.domain;

import java.util.Arrays;
import java.util.Comparator;

public class FareDistance {
    private enum Policy {
        HALF(10, 5),
        FULL(50, 8);

        private final int distance;
        private final int per;

        Policy(int distance, int per) {
            this.distance = distance;
            this.per = per;
        }

        public int getDistance() {
            return distance;
        }

        public static int calculateOverFare(int distance) {
            return Arrays.stream(values())
                    .sorted(Comparator.comparing(Policy::getDistance).reversed())
                    .filter(fareDistance -> fareDistance.distance < distance)
                    .findFirst()
                    .map(fareDistance -> overFare(distance, fareDistance))
                    .orElse(0);
        }

        private static int overFare(int distance, Policy fareDistance) {
            return (int) ((Math.ceil((distance - 1) / fareDistance.per) + 1) * 100);
        }

    }

    private final int distance;

    public FareDistance(int distance) {
        this.distance = distance;
    }

    public int getAmountFare() {
        return Policy.calculateOverFare(this.distance);
    }
}
