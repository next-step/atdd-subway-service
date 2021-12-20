package nextstep.subway.path.domain.fare;

public class TotalFarePolicy implements FarePolicy<Integer> {
    private final FarePolicy<Integer> distancePolicy;
    private final FarePolicy<Fare> agePolicy;

    public TotalFarePolicy(FarePolicy<Integer> distancePolicy, FarePolicy<Fare> agePolicy) {
        this.distancePolicy = distancePolicy;
        this.agePolicy = agePolicy;
    }

    @Override
    public Fare calculateFare(Integer distance) {
        Fare fare = distancePolicy.calculateFare(distance);
        return agePolicy.calculateFare(fare);
    }
}
