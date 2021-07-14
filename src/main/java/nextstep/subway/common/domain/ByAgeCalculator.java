package nextstep.subway.common.domain;


import java.math.BigDecimal;
import java.util.Arrays;

public class ByAgeCalculator implements FareCaculator<BigDecimal, Integer> {

    @Override
    public BigDecimal calculate(BigDecimal subwayFare, Integer age) {
        return discount(subwayFare, age);
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

    private static BigDecimal caculateDiscountedFare(BigDecimal chargedPrice, BigDecimal excludedPrice, BigDecimal payoutRate) {
        return chargedPrice.subtract(excludedPrice).multiply(payoutRate).setScale(0,BigDecimal.ROUND_HALF_UP);
    }




}
