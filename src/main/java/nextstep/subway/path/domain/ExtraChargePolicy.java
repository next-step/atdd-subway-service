package nextstep.subway.path.domain;

import nextstep.subway.auth.domain.Money;
import nextstep.subway.line.domain.Distance;

public abstract class ExtraChargePolicy {

    protected final Money extraUnit = Money.from(100);
    protected Distance distance;

    public abstract Money getExtraCharge();

    public static ExtraChargePolicy fromDistance(Distance distance) {
        if (distance.isUnderEqualTo(10)) {
            return new UnderTenChargePolicy(distance);
        }
        if (distance.isOver(10) && distance.isUnderEqualTo(50)) {
            return new BetweenTenAndFiftyChargePolicy(distance);
        }
        return new OverFiftyChargePolicy(distance);
    }
}
