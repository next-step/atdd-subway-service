package nextstep.subway.line.application;

import nextstep.subway.line.domain.Fare;
import nextstep.subway.line.domain.Money;
import nextstep.subway.line.domain.fare.policy.BaseFarePolicy;

public interface FarePolicyHandler {

    Money apply(Fare fare);

    void link(BaseFarePolicy subwayBasePolicy);
}
