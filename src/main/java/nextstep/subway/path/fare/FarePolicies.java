package nextstep.subway.path.fare;

import nextstep.subway.line.domain.Distance;
import nextstep.subway.path.domain.Fare;

import java.util.List;

public class FarePolicies {

    private final List<FarePolicy> farePolicyList;

    public FarePolicies(List<FarePolicy> farePolicyList) {
        this.farePolicyList = farePolicyList;
    }

    public Fare calculate(int distance) {
        return farePolicyList.stream()
                .map(farePolicy -> farePolicy.calculateFare(Distance.valueOf(distance)))
                .reduce(Fare.ZERO, Fare::add);
    }
}
