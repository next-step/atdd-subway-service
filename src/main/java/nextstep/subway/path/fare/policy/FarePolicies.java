package nextstep.subway.path.fare.policy;

import nextstep.subway.line.domain.Distance;
import nextstep.subway.path.domain.exception.InvalidFarePolicyException;
import nextstep.subway.path.fare.Fare;

import java.util.List;

public class FarePolicies {

    private final List<FarePolicy> farePolicyList;

    public FarePolicies(List<FarePolicy> farePolicyList) {
        this.farePolicyList = farePolicyList;
    }

    public Fare calculate(int distance) {
        verifyValidDistance(distance);
        return farePolicyList.stream()
                .map(farePolicy -> farePolicy.calculateFare(Distance.valueOf(distance)))
                .reduce(Fare.ZERO, Fare::add);
    }

    private void verifyValidDistance(int distance) {
        if (distance < 1) {
            throw new InvalidFarePolicyException();
        }
    }
}
