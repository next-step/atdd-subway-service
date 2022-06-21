package nextstep.subway.fare.domain;

public class DistanceFare implements FarePolicy {
    @Override
    public int calculateFare(int distance) {
        return Fare.calculateFare(distance);
    }
}
