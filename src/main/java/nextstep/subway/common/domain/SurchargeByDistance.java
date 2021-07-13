package nextstep.subway.common.domain;

import java.math.BigDecimal;
import java.math.RoundingMode;

public enum SurchargeByDistance {

    BASIC_SECTION(0, 10, BigDecimal.ZERO, 0),
    MEDIUM_SECTION(10, 50, BigDecimal.valueOf(100), 5),
    OVER_SECTION(50, Integer.MAX_VALUE, BigDecimal.valueOf(100), 8);


    private int minDistance;
    private int maxDistance;
    private BigDecimal surcharge;
    private int perDistance;

    SurchargeByDistance(int minDistance, int maxDistance, BigDecimal surcharge, int perDistance) {
        this.minDistance = minDistance;
        this.maxDistance = maxDistance;
        this.surcharge = surcharge;
        this.perDistance = perDistance;
    }

    public static BigDecimal charge(BigDecimal chargedFare, int distance) {
        return chargedFare.add(surchargedFare(distance));

    }

    private static BigDecimal surchargedFare(int distance) {
        if (BASIC_SECTION.maxDistance >= distance) {
            return BASIC_SECTION.getSurcharge();
        }
        if (MEDIUM_SECTION.maxDistance >= distance) {
            return chargeMediumSection(MEDIUM_SECTION.getSurcharge(), MEDIUM_SECTION.getPerDistance(), distance - MEDIUM_SECTION.getMinDistance() );
        }
        return chargeOverSection(OVER_SECTION.getSurcharge(), OVER_SECTION.getPerDistance(), distance - OVER_SECTION.getMinDistance());
    }

    private static BigDecimal chargeOverSection(BigDecimal surcharge, int perDistance, int extraDistance) {
        return maxChargedMedium().add(surcharge.multiply(calculateChargeTimes(perDistance, extraDistance)));
    }

    private static BigDecimal maxChargedMedium() {
        return chargeMediumSection(MEDIUM_SECTION.getSurcharge(), MEDIUM_SECTION.getPerDistance(), MEDIUM_SECTION.MEDIUM_SECTION.getMaxDistance() - MEDIUM_SECTION.minDistance );
    }

    private static BigDecimal chargeMediumSection(BigDecimal surcharge, int perDistance, int extraDistance) {

        return surcharge.multiply(calculateChargeTimes(perDistance, extraDistance));
    }

    private static BigDecimal calculateChargeTimes(int perDistance, int extraDistance) {
        return BigDecimal.valueOf((double) extraDistance/perDistance).setScale(0, RoundingMode.UP);
    }


    public int getMinDistance() {
        return minDistance;
    }

    public int getMaxDistance() {
        return maxDistance;
    }

    public BigDecimal getSurcharge() {
        return surcharge;
    }

    public int getPerDistance() {
        return perDistance;
    }
}
