package nextstep.subway.fare.utils;

import java.util.List;

import nextstep.subway.fare.domain.Fare;
import nextstep.subway.fare.policy.path.PathAdditionalPolicy;
import nextstep.subway.path.domain.Path;

public class FareCalculator {

    private FareCalculator() {
    }

    public static Fare calculate(Path path) {
        Fare fare = new Fare();
        List<PathAdditionalPolicy> policies = path.getPolicies();
        for (PathAdditionalPolicy policy : policies) {
            fare = policy.apply(fare, path);
        }

        return fare;
    }
}
