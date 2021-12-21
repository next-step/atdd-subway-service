package nextstep.subway.path.domain.fare;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class TotalFarePolicy implements FarePolicy<Integer> {
    private final FarePolicy<Integer> distancePolicy;
    private final List<FarePolicy<Fare>> farePolicies;

    public TotalFarePolicy(FarePolicy<Integer> distancePolicy, FarePolicy<Fare>... farePolicies) {
        this.distancePolicy = distancePolicy;
        this.farePolicies = Arrays.stream(farePolicies)
            .collect(Collectors.toList());
    }

    @Override
    public Fare calculateFare(Integer distance) {
        Fare fare = distancePolicy.calculateFare(distance);

        for (FarePolicy<Fare> farePolicy : farePolicies) {
            fare = farePolicy.calculateFare(fare);
        }

        return fare;
    }
}
