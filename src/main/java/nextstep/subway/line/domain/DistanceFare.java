package nextstep.subway.line.domain;

import static nextstep.subway.line.domain.OverFareByDistance.FIRST;
import static nextstep.subway.line.domain.OverFareByDistance.SECOND;

public class DistanceFare {
    private static final int BASIC_FARE = 1250;

    private final int distance;

    public DistanceFare(int distance) {
        this.distance = distance;
    }

    public int getFare() {
        return BASIC_FARE + getOverFare();
    }

    private int getOverFare() {
        return OverFareByDistance.of(distance)
                .map(it -> {
                    int overFare = it.calculateOverFare(distance);
                    if (it == SECOND) {
                        overFare += FIRST.calculateOverFare(SECOND.getMinDistance());
                    }
                    return overFare;
                }).orElse(0);
    }
}
