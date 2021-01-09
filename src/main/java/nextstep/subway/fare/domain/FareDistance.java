package nextstep.subway.fare.domain;

import java.util.Arrays;
import java.util.Comparator;

public class FareDistance extends OverCharge {
    private enum Policy {
        HALF(10, 5),
        FULL(50, 8);

        private static final int BASIC_FARE = 1250;
        private final int distance;
        private final int eachDistance;

        Policy(int distance, int eachDistance) {
            this.distance = distance;
            this.eachDistance = eachDistance;
        }

        public int getDistance() {
            return distance;
        }

        public static int calculateOverFare(int distance) {
            return Arrays.stream(values())
                    .sorted(Comparator.comparing(Policy::getDistance).reversed())
                    .filter(fareDistance -> fareDistance.distance < distance)
                    .findFirst()
                    .map(fareDistance -> BASIC_FARE + overFare(distance, fareDistance))
                    .orElse(BASIC_FARE);
        }

        private static int overFare(int distance, Policy fareDistance) {
            return (int) ((Math.ceil((distance - 1) / fareDistance.eachDistance) + 1) * 100);
        }

    }

    private final int distance;

    public FareDistance(int distance) {
        this.distance = distance;
    }

    public int getAmountFare() {
        return Policy.calculateOverFare(this.distance);
    }

    @Override
    public int getAmount() {
        return Policy.calculateOverFare(this.distance);
    }
}
