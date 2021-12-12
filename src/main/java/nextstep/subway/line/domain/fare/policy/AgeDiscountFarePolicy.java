package nextstep.subway.line.domain.fare.policy;

import java.util.List;
import nextstep.subway.line.domain.Fare;
import nextstep.subway.line.domain.Money;
import nextstep.subway.line.domain.fare.policycondition.AgeDiscountFareCondition;

public abstract class AgeDiscountFarePolicy implements BaseFarePolicy {

    @Override
    public Money getCalculateFare(Fare fare, Money money) {
        for (AgeDiscountFareCondition each : getConditions()) {
            money = each.discountFare(money, fare.getAge());
        }

        return money;
    }

    protected abstract List<AgeDiscountFareCondition> getConditions();
}
