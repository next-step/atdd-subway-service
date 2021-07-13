package nextstep.subway.path.domain;

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

    public float getPayoutRate() {
        return payoutRate;
    }

    public static BigDecimal discount(BigDecimal chargedFare, Integer userAge) {
        if (userAge == null) {
            return chargedFare;
        }
        return Arrays.stream(DiscountByAge.values())
                .filter(discountByAge -> discountByAge.getMaxAge() >= userAge)
                .findFirst()
                .map(discountByAge -> caculateDiscountedFare(chargedFare, discountByAge.getExcludedPrice(), discountByAge.getPayoutRate()))
                .orElse(chargedFare);
    }

    private static BigDecimal caculateDiscountedFare(BigDecimal chargedPrice, BigDecimal excludedPrice, float payoutRate) {
        return chargedPrice.subtract(excludedPrice).multiply(BigDecimal.valueOf(payoutRate)).setScale(0,BigDecimal.ROUND_HALF_UP);
    }


}
