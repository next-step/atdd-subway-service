package nextstep.subway.path.domain.fee.distanceFee;

import nextstep.subway.path.domain.exceptions.InvalidFeeDistanceException;

import java.math.BigDecimal;

public class LongDistanceFee implements DistanceFee {
    private static final Integer MIN_DISTANCE = 10;
    private static final Integer MAX_DISTANCE = 51;
    private static final BigDecimal DEFAULT_FEE = BigDecimal.valueOf(1250);

    private final Integer distance;

    public LongDistanceFee(final Integer distance) {
        validate(distance);
        this.distance = distance;
    }

    @Override
    public BigDecimal calculate() {
        Integer extraFee = (int) (Math.ceil((distance - MIN_DISTANCE - 1) / 5) + 1) * 100;
        return DEFAULT_FEE.add(BigDecimal.valueOf(extraFee));
    }

    private void validate(Integer distance) {
        if (distance <= MIN_DISTANCE || distance >= MAX_DISTANCE) {
            throw new InvalidFeeDistanceException("10km 초과 ~ 50km 이하 거리만 계산 가능합니다.");
        }
    }
}
