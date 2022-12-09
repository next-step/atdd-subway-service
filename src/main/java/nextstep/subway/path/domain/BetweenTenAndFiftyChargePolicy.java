package nextstep.subway.path.domain;

import nextstep.subway.auth.domain.Money;
import nextstep.subway.line.domain.Distance;

public class BetweenTenAndFiftyChargePolicy extends ExtraChargePolicy {

    private static final int PER_FIVE = 5;

    public BetweenTenAndFiftyChargePolicy(Distance distance) {
        this.distance = distance;
    }

    @Override
    public Money getExtraCharge() {
        return extraUnit.mul(distance.countPerSize(PER_FIVE));
    }
}
