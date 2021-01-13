package nextstep.subway.fares.policy;

import nextstep.subway.fares.domain.Fare;
import nextstep.subway.line.domain.Section;
import nextstep.subway.path.domain.Distance;

import java.util.ArrayList;
import java.util.List;

public class DistanceBasedFarePolicy implements FarePolicy {

    private final List<DistanceBasedFarePolicyDetails> policies;

    public DistanceBasedFarePolicy() {
        policies = new ArrayList<>();
        policies.add(new DistanceBasedFarePolicyFirst());
        policies.add(new DistanceBasedFarePolicySecond());
        policies.add(new DistanceBasedFarePolicyThird());
    }

    @Override
    public void calculateFare(Fare fare, Distance distance, List<Section> sections) {
        Distance currDistance = new Distance();
        for (DistanceBasedFarePolicyDetails policy : policies) {
            policy.calculateFareByDistance(currDistance, fare, distance);
        }
    }
}
