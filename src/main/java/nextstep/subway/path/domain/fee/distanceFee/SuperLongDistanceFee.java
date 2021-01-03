package nextstep.subway.path.domain.fee.distanceFee;

import nextstep.subway.path.domain.exceptions.InvalidFeeDistanceException;

import java.math.BigDecimal;

public class SuperLongDistanceFee implements DistanceFee {
    private static final Integer MIN_DISTANCE = 51;
    private static final BigDecimal DEFAULT_FEE = BigDecimal.valueOf(2050);

    private final Integer distance;

    public SuperLongDistanceFee(Integer distance) {
        validate(distance);
        this.distance = distance;
    }

    @Override
    public BigDecimal calculate() {
        Integer fee = (int) (Math.ceil((distance - MIN_DISTANCE - 1) / 8) + 1) * 100;
        return DEFAULT_FEE.add(BigDecimal.valueOf(fee));
    }

    private void validate(Integer distance) {
        if (distance < MIN_DISTANCE) {
            throw new InvalidFeeDistanceException("50km 초과부터만 계산할 수 있습니다.");
        }
    }
}
