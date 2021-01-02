package nextstep.subway.path.domain.fee;

import nextstep.subway.path.domain.exceptions.InvalidFeeDistanceException;

import java.math.BigDecimal;

public class DefaultFee implements Fee {
    private static final Double MIN_DISTANCE = 0d;
    private static final Double MAX_DISTANCE = 10d;

    private final Double distance;

    public DefaultFee(Double distance) {
        validate(distance);
        this.distance = distance;
    }
    @Override
    public BigDecimal calculate() {
        return BigDecimal.valueOf(1250L);
    }

    private void validate(Double distance) {
        if (distance < MIN_DISTANCE || distance > MAX_DISTANCE) {
            throw new InvalidFeeDistanceException("기본 요금은 0km 이상 ~ 10km 이하여야 합니다.");
        }
    }
}
