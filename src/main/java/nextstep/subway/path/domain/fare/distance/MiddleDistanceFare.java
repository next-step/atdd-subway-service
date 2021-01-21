package nextstep.subway.path.domain.fare.distance;

import nextstep.subway.path.domain.fare.distance.DistanceFare;

public class MiddleDistanceFare extends DistanceFare {
    private static final int FIVE_KM_DISTANCE = 5;
    private static final int TEN_KM_DISTANCE = 10;
    private final int distance;

    public MiddleDistanceFare(int distance) {
        this.distance = distance;
    }

    @Override
    public int calculateFare() {
        return calculateOverFare(distance - TEN_KM_DISTANCE, FIVE_KM_DISTANCE);
    }
}
