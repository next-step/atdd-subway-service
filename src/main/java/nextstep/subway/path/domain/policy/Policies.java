package nextstep.subway.path.domain.policy;

import java.util.ArrayList;
import java.util.List;

public class Policies {

    private final List<FarePolicy> policies;

    public Policies(List<FarePolicy> policies) {
        this.policies = policies;
    }

    public static Policies of(FarePolicy ...policies) {
        List<FarePolicy> initializedPolicies = new ArrayList<>();
        for (FarePolicy policy : policies) {
            initializedPolicies.add(policy);
        }
        return new Policies(initializedPolicies);
    }

    public int calculate(int fareValue) {
        return 0;
    }
}
