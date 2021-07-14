package nextstep.subway.common.domain;


import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;

import static nextstep.subway.common.domain.SurchargeByDistance.*;

public class ByAgeCalculator implements FareCaculator<SubwayFare, Integer> {

    @Override
    public SubwayFare calculate(SubwayFare subwayFare, Integer age) {
        return new SubwayFare(discount(subwayFare.charged(), age));
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
