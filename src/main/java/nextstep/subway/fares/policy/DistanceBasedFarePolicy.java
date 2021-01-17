package nextstep.subway.fares.policy;

import nextstep.subway.fares.domain.Fare;
import nextstep.subway.path.domain.Distance;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class DistanceBasedFarePolicy implements FarePolicy {

    private final List<DistanceBasedFarePolicyDetails> policies;

    public DistanceBasedFarePolicy() {
        policies = Collections.unmodifiableList(Arrays.asList(
                new DistanceBasedFarePolicyFirst(),
                new DistanceBasedFarePolicySecond(),
                new DistanceBasedFarePolicyThird()
        ));
    }

    @Override
    public void calculateFare(Fare fare, FareContext fareContext) {
        Distance currDistance = new Distance();
        for (DistanceBasedFarePolicyDetails policy : policies) {
            policy.calculateFareByDistance(currDistance, fare, fareContext.getPath().getDistance());
        }
    }
}
