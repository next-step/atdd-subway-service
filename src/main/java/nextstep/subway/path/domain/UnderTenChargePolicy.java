package nextstep.subway.path.domain;

import nextstep.subway.auth.domain.Money;
import nextstep.subway.line.domain.Distance;

public class UnderTenChargePolicy extends ExtraChargePolicy {
    public UnderTenChargePolicy(Distance distance) {
        this.distance = distance;
    }

    @Override
    public Money getExtraCharge() {
        return Money.zero();
    }
}
