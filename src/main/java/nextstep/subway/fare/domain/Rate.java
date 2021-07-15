package nextstep.subway.fare.domain;

import java.math.BigDecimal;

import static java.math.BigDecimal.ZERO;
import static java.math.BigDecimal.valueOf;

public class Rate {
    private BigDecimal value = ZERO;

    public Rate(BigDecimal value) {
        validateContructor(value);
        this.value = value;
    }

    public Rate(int value) {
        BigDecimal castingValue = valueOf(value);
        validateContructor(castingValue);
        this.value = castingValue;
    }

    private void validateContructor(BigDecimal value) {
        if(value.compareTo(ZERO) < 0) {
            throw new IllegalArgumentException("비율은 0보다 작을 수 없습니다.");
        }
    }

    public BigDecimal getValue() {
        return value;
    }
}
