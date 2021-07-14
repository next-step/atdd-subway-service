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
