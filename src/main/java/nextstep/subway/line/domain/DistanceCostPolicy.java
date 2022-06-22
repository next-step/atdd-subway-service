package nextstep.subway.line.domain;

import static nextstep.subway.line.domain.ExceedCharge.FIFTY_EXCEED;
import static nextstep.subway.line.domain.ExceedCharge.TEN_EXCEED;

public class DistanceCostPolicy implements OperationCostPolicy {
    private static final long ZERO = 0;
    private static final long DEFAULT_CHARGE = 1250;
    private static final long NO_EXCEED_DISTANCE = 10;
    private static final long FIFTY_EXCEED_DISTANCE = 50;

    @Override
    public Charge basicCharge() {
        return new Charge(DEFAULT_CHARGE);
    }

    @Override
    public Charge policyCharge(long distance) {
        if (distance <= NO_EXCEED_DISTANCE) {
            return new Charge(ZERO);
        }
        final long lastExceedDistance = Math.max(distance - FIFTY_EXCEED_DISTANCE, ZERO);
        final Charge tenExceedCharge = TEN_EXCEED.calculateOverFare(distance - NO_EXCEED_DISTANCE - lastExceedDistance);
        return tenExceedCharge.plus(FIFTY_EXCEED.calculateOverFare(lastExceedDistance));
    }
}
