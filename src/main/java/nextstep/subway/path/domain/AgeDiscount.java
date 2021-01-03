package nextstep.subway.path.domain;

import java.math.BigDecimal;

public interface AgeDiscount {
    BigDecimal apply(BigDecimal fee);
}
