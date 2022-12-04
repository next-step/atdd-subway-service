package nextstep.subway.path.fare.policy;

import nextstep.subway.line.domain.Distance;
import nextstep.subway.path.fare.Fare;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DistanceFarePolicies {

    private final List<FarePolicy> farePolicyList;

    public DistanceFarePolicies(List<FarePolicy> farePolicyList) {
        this.farePolicyList = farePolicyList;
    }

    public Fare calculate(Distance distance) {
        return farePolicyList.stream()
                .map(farePolicy -> farePolicy.calculateFare(distance))
                .reduce(Fare.ZERO, Fare::add);
    }

    public Fare calculate(int distance) {
        return calculate(Distance.valueOf(distance));
    }
}
