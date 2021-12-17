package nextstep.subway.path.domain.fare;

import java.util.Arrays;
import java.util.List;

import nextstep.subway.path.application.FarePolicy;

public class TotalFarePolicy implements FarePolicy {
    private static final List<FarePolicy> farePolicies = Arrays.asList(
        new BasicFarePolicy(),
        DistanceFarePolicy.TEN_TO_FIFTY,
        DistanceFarePolicy.OVER_FIFTY);

    @Override
    public Fare calculateFare(int distance) {
        return farePolicies.stream()
            .map(farePolicy -> farePolicy.calculateFare(distance))
            .reduce(Fare.ZERO, (Fare::add));
    }
}
