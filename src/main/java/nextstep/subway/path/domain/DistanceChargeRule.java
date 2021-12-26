package nextstep.subway.path.domain;

import java.util.Arrays;

public enum DistanceChargeRule {
    FROM_0KM_TO_10KM(0, 10, 0, 0),
    FROM_10KM_TO_50KM(10, 50, 5, 100),
    OVER_50KM(50, Integer.MAX_VALUE, 8, 100);

    private int startPartition;
    private int endPartition;
    private int perDistance;
    private int charge;

    DistanceChargeRule(int startPartition, int endPartition, int perDistance, int charge) {
        this.startPartition = startPartition;
        this.endPartition = endPartition;
        this.perDistance = perDistance;
        this.charge = charge;
    }

    public static int calculateChargeByDistance(int distance) {
        DistanceChargeRule rule = valueOf(distance);
        if (rule.equals(OVER_50KM)) {
            return calculateChargeByDistance(FROM_10KM_TO_50KM.endPartition) + calculationFormula(distance, rule);
        }
        if (rule.equals(FROM_10KM_TO_50KM)) {
            return calculationFormula(distance, rule);
        }
        return 0;
    }

    private static int calculationFormula(int distance, DistanceChargeRule rule) {
        return ((int) Math.floor((double) (distance - rule.startPartition) / rule.perDistance)) * rule.charge;
    }

    private static DistanceChargeRule valueOf(int distance) {
        return Arrays.stream(values())
                .filter(rule -> rule.isInclude(distance))
                .findFirst()
                .orElse(FROM_0KM_TO_10KM);
    }

    private boolean isInclude(int distance) {
        return this.startPartition < distance && this.endPartition >= distance;
    }
}
