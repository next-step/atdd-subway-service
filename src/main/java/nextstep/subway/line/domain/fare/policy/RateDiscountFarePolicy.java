package nextstep.subway.line.domain.fare.policy;

import java.util.List;
import nextstep.subway.line.domain.Fare;
import nextstep.subway.line.domain.Money;
import nextstep.subway.line.domain.fare.policycondition.RateDiscountFareCondition;

public abstract class RateDiscountFarePolicy implements BaseFarePolicy {

    @Override
    public Money getCalculateFare(Fare fare, Money money) {
        for (RateDiscountFareCondition each : getConditions()) {
            money = each.discountFare(money, fare.getAge());
        }

        return money;
    }

    protected abstract List<RateDiscountFareCondition> getConditions();
}
