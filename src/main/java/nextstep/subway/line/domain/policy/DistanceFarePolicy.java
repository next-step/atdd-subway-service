package nextstep.subway.line.domain.policy;

import java.util.List;
import nextstep.subway.line.domain.Fare;
import nextstep.subway.line.domain.Money;
import nextstep.subway.line.domain.policycondition.DistancePolicyCondition;

public abstract class DistanceFarePolicy implements BaseFarePolicy {

    @Override
    public Money getCalculateFare(Fare fare, Money money) {//int distance, Money fare
        for (DistancePolicyCondition each : getConditions()) {
            money = money.plus(each.calculateFare(fare.getDistance()));
        }

        return money;
    }

    protected abstract List<DistancePolicyCondition> getConditions();
}
