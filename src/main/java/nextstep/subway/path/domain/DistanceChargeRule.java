package nextstep.subway.path.domain;

import java.util.Arrays;

public enum DistanceChargeRule {

    FROM_51KM_OVER(51L, Long.MAX_VALUE, 8L, 100, 50L),
    FROM_11KM_TO_50KM(11L, 50L, 5L, 100, 10L),
    FROM_0KM_TO_10KM(0L, 10L, 0L, 0, 0L);

    private long firstStandard;
    private long lastStandard;
    private long perDistance;
    private int forCharge;
    private long divisionStandard;

    DistanceChargeRule(long firstStandard, long lastStandard, long perDistance, int forCharge, long divisionStandard) {
        this.firstStandard = firstStandard;
        this.lastStandard = lastStandard;
        this.perDistance = perDistance;
        this.forCharge = forCharge;
        this.divisionStandard = divisionStandard;
    }

    public static DistanceChargeRule valueOf(long distance) {
        return Arrays.stream(values())
                .filter(rule -> rule.isInclude(distance))
                .findFirst()
                .orElse(FROM_0KM_TO_10KM);
    }

    private boolean isInclude(long distance) {
        return this.firstStandard <= distance && this.lastStandard >= distance;
    }

    public static long calculateFare(Long distance) {
        DistanceChargeRule rule = valueOf(distance);
        if (rule.equals(FROM_51KM_OVER)) {
            return calculateFare(rule.divisionStandard) + fareFormula(distance, rule);
        }
        if (rule.equals(FROM_11KM_TO_50KM)) {
            return fareFormula(distance, rule);
        }
        return 0L;
    }

    private static long fareFormula(long distance, DistanceChargeRule rule) {
        return (long) (Math.ceil(((double) distance - rule.divisionStandard) / rule.perDistance)) * rule.forCharge;
    }
}
