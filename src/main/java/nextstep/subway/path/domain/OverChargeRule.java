package nextstep.subway.path.domain;

import java.math.BigInteger;
import java.util.Arrays;

public enum OverChargeRule {

    FROM_51KM_OVER(51L, Long.MAX_VALUE, 8L, 100),
    FROM_11KM_TO_50KM(11L, 50L, 5L, 100),
    FROM_0KM_TO_10KM(0L, 10L, 0L, 0);

    private long firstStandard;
    private long lastStandard;
    private long perDistance;
    private int forCharge;

    OverChargeRule(long firstStandard, long lastStandard, long perDistance, int forCharge) {
        this.firstStandard = firstStandard;
        this.lastStandard = lastStandard;
        this.perDistance = perDistance;
        this.forCharge = forCharge;
    }

    public static OverChargeRule valueOf(long distance) {
        return Arrays.stream(values())
                .filter(rule -> rule.isInclude(distance))
                .findFirst()
                .orElse(FROM_0KM_TO_10KM);
    }

    private boolean isInclude(long distance) {
        return this.firstStandard <= distance && this.lastStandard >= distance;
    }

    public static BigInteger calculateOverFare(Long distance) {
        OverChargeRule rule = valueOf(distance);
        if (rule != FROM_0KM_TO_10KM) {
            Double charge = ((Math.ceil((distance - 1) / rule.perDistance) + 1) * rule.forCharge);
            return new BigInteger(String.format("%.0f", charge));
        }
        return new BigInteger("0");
    }

    public long getFirstStandard() {
        return firstStandard;
    }

    public long getLastStandard() {
        return lastStandard;
    }

    public long getPerDistance() {
        return perDistance;
    }

    public int getForCharge() {
        return forCharge;
    }
}
