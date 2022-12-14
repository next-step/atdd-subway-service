package nextstep.subway.fare.domain;

import java.util.Arrays;

public enum DistanceExtraFare {
    NEAR(10, 50, 100, 5),
    FAR(50, 500, 100, 8);

    private final int distanceStart;
    private final int distanceEnd;
    private final int fare;
    private final int unitDistanceInKiloMeter;

    DistanceExtraFare(int distanceStart, int distanceEnd, int fare, int unitDistanceInKiloMeter) {

        this.distanceStart = distanceStart;
        this.distanceEnd = distanceEnd;
        this.fare = fare;
        this.unitDistanceInKiloMeter = unitDistanceInKiloMeter;
    }

    public static int calculate(int distance) {
        return Arrays.stream(values())
            .map(distanceExtraFare -> distanceExtraFare.calculateBy(distance))
            .reduce(Integer::sum)
            .orElse(0);
    }

    private int calculateBy(int distance) {
        if (distance <= this.distanceStart) {
            return 0;
        }
        if (distance > this.distanceEnd) {
            distance = this.distanceEnd;
        }
        return ((distance - this.distanceStart) / this.unitDistanceInKiloMeter) * this.fare;
    }

    public int getFare() {
        return this.fare;
    }
}
