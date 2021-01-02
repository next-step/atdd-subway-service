package nextstep.subway.path.domain.fee;

import nextstep.subway.path.domain.exceptions.InvalidFeeDistanceException;

import java.math.BigDecimal;

public class DefaultFee implements Fee {
    private static final Integer MIN_DISTANCE = 0;
    private static final Integer MAX_DISTANCE = 10;

    private final Integer distance;

    public DefaultFee(Integer distance) {
        validate(distance);
        this.distance = distance;
    }
    @Override
    public BigDecimal calculate() {
        return BigDecimal.valueOf(1250L);
    }

    private void validate(Integer distance) {
        if (distance < MIN_DISTANCE || distance > MAX_DISTANCE) {
            throw new InvalidFeeDistanceException("기본 요금은 0km 이상 ~ 10km 이하여야 합니다.");
        }
    }
}
