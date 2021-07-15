package nextstep.subway.common.domain;

import java.math.BigDecimal;
import java.util.Arrays;

public enum DiscountByAge {
    KID(0, 6, BigDecimal.ZERO, 0f),
    CHILD(7, 13, BigDecimal.valueOf(350), 0.5f),
    TEENAGER(14, 18, BigDecimal.valueOf(350), 0.8f),
    NORMAL(19, 65, BigDecimal.ZERO, 1f),
    ELDERS(66, Integer.MAX_VALUE, BigDecimal.ZERO, 0f);

    private int minAge;
    private int maxAge;
    private BigDecimal excludedPrice;
    private float payoutRate;

    DiscountByAge(int minAge, int maxAge, BigDecimal excludedPrice, float payoutRate) {
        this.minAge = minAge;
        this.maxAge = maxAge;
        this.excludedPrice = excludedPrice;
        this.payoutRate = payoutRate;
    }

    public int getMinAge() {
        return minAge;
    }

    public int getMaxAge() {
        return maxAge;
    }

    public BigDecimal getExcludedPrice() {
        return excludedPrice;
    }

    public BigDecimal getPayoutRate() {
        return BigDecimal.valueOf(payoutRate);
    }



}
