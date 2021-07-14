package nextstep.subway.common.domain;


import java.math.BigDecimal;
import java.math.RoundingMode;

import static nextstep.subway.common.domain.SurchargeByDistance.*;

public class ByDistanceCalculator implements FareCaculator<SubwayFare, Integer> {

    @Override
    public SubwayFare calculate(SubwayFare subwayFare, Integer distance) {
        return subwayFare.plus(surchargedFare(distance));
    }


    private static BigDecimal surchargedFare(int distance) {
        if (BASIC_SECTION.getMaxDistance() >= distance) {
            return BASIC_SECTION.getSurcharge();
        }
        if (MEDIUM_SECTION.getMaxDistance() >= distance) {
            return chargeMediumSection(MEDIUM_SECTION.getSurcharge(), MEDIUM_SECTION.getPerDistance(), distance - MEDIUM_SECTION.getMinDistance() );
        }
        return chargeOverSection(OVER_SECTION.getSurcharge(), OVER_SECTION.getPerDistance(), distance - OVER_SECTION.getMinDistance());
    }

    private static BigDecimal chargeOverSection(BigDecimal surcharge, int perDistance, int extraDistance) {
        return maxChargedMedium().add(surcharge.multiply(calculateChargeTimes(perDistance, extraDistance)));
    }

    private static BigDecimal maxChargedMedium() {
        return chargeMediumSection(MEDIUM_SECTION.getSurcharge(), MEDIUM_SECTION.getPerDistance(), SurchargeByDistance.MEDIUM_SECTION.getMaxDistance() - MEDIUM_SECTION.getMinDistance() );
    }

    private static BigDecimal chargeMediumSection(BigDecimal surcharge, int perDistance, int extraDistance) {

        return surcharge.multiply(calculateChargeTimes(perDistance, extraDistance));
    }

    private static BigDecimal calculateChargeTimes(int perDistance, int extraDistance) {
        return BigDecimal.valueOf((double) extraDistance/perDistance).setScale(0, RoundingMode.UP);
    }



}
