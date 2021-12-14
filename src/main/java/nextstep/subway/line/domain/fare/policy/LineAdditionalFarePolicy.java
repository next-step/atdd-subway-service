package nextstep.subway.line.domain.fare.policy;

import nextstep.subway.line.domain.Fare;
import nextstep.subway.line.domain.Money;

public class LineAdditionalFarePolicy implements BaseFarePolicy {

    @Override
    public Money getCalculateFare(Fare fare, Money money) {
        return fare.getMaxAdditionalFare();
    }
}
