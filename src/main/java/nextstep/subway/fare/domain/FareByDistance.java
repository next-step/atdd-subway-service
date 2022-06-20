package nextstep.subway.fare.domain;

public class FareByDistance implements FarePolicy {
    @Override
    public int calculateFare(int distance) {
        return Fare.calculateFare(distance);
    }
}
