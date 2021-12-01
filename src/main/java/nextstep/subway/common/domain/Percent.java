package nextstep.subway.common.domain;

import java.math.BigDecimal;
import java.math.RoundingMode;
import org.springframework.util.Assert;

public final class Percent {

    private static final BigDecimal PERCENTAGE = BigDecimal.valueOf(100);
    private static final int MIN = 0;
    private static final int MAX = 100;

    private final BigDecimal value;

    private Percent(int value) {
        Assert.isTrue(withinRange(value),
            String.format("값(%d)은 %d부터 %d까지만 가능합니다.", value, MIN, MAX));
        this.value = BigDecimal.valueOf(value);
    }

    public static Percent from(int value) {
        return new Percent(value);
    }

    public int percentageOf(int target) {
        return BigDecimal.valueOf(target)
            .multiply(value)
            .divide(PERCENTAGE, 0, RoundingMode.FLOOR)
            .intValue();
    }

    private boolean withinRange(int value) {
        return equalOrGreaterThanMin(value) && equalOrLessThanMax(value);
    }

    private boolean equalOrGreaterThanMin(int value) {
        return MIN <= value;
    }

    private boolean equalOrLessThanMax(int value) {
        return value <= MAX;
    }
}
