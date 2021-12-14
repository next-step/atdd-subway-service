package nextstep.subway.line.domain.fare.policy;


import nextstep.subway.line.domain.Fare;
import nextstep.subway.line.domain.Money;

public interface BaseFarePolicy {

    Money getCalculateFare(Fare fare, Money money);
}
