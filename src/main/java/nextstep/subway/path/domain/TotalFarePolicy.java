package nextstep.subway.path.domain;

import java.util.Arrays;
import java.util.List;

public class TotalFarePolicy implements FarePolicy {
    private static final List<FarePolicy> farePolicies = Arrays.asList(
        new BasicFarePolicy(),
        new TenToFiftyFarePolicy(),
        new OverFiftyFarePolicy());

    @Override
    public Fare calculateFare(int distance) {
        return farePolicies.stream()
            .map(farePolicy -> farePolicy.calculateFare(distance))
            .reduce(Fare.ZERO, (Fare::add));
    }
}
