package nextstep.subway.line.infrastructure.fare.policy;

import java.util.ArrayList;
import java.util.List;
import nextstep.subway.line.application.FarePolicyHandler;
import nextstep.subway.line.domain.Fare;
import nextstep.subway.line.domain.Money;
import nextstep.subway.line.domain.fare.policy.BaseFarePolicy;

public class FarePolicyHandlerImpl implements FarePolicyHandler {

    private final List<BaseFarePolicy> policies = new ArrayList<>();

    @Override
    public Money apply(Fare fare) {
        Money result = Money.won(0);
        for (BaseFarePolicy policy : policies) {
            result = policy.getCalculateFare(fare, result);
        }

        return result;
    }

    @Override
    public void link(BaseFarePolicy subwayBasePolicy) {
        policies.add(subwayBasePolicy);
    }
}
