package nextstep.subway.path.domain.fare.distance;

public class BasicDistanceFare extends DistanceFare {
    private final int distance;

    public BasicDistanceFare(int distance) {
        this.distance = distance;
    }

    @Override
    public int calculateFare() {
        return NO_FARE;
    }
}
