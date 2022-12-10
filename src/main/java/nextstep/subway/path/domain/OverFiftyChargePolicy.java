package nextstep.subway.path.domain;

import nextstep.subway.auth.domain.Money;
import nextstep.subway.line.domain.Distance;

public class OverFiftyChargePolicy extends ExtraChargePolicy {

    private static final int PER_EIGHT = 8;

    public OverFiftyChargePolicy(Distance distance) {
        this.distance = distance;
    }

    @Override
    public Money getExtraCharge() {
        return extraUnit.mul(distance.countPerSize(PER_EIGHT))
                .getCharge();
    }
}
