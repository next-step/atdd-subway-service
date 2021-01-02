package nextstep.subway.path.domain.fee;

import java.math.BigDecimal;

public class LongFee implements Fee {
    private static final BigDecimal DEFAULT_FEE = BigDecimal.valueOf(1250L);
    private static final Double DEFAULT_DISTANCE = 10d;

    private final Double distance;

    public LongFee(final Double distance) {
        this.distance = distance;
    }

    @Override
    public BigDecimal calculate() {
        return null;
    }
}
