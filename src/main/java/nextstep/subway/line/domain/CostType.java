package nextstep.subway.line.domain;

import static java.util.Arrays.*;

public enum CostType {
    ADULT(19, 0, 0),
    YOUTH(13, 350, 0.2),
    CHILD(6, 350, 0.5),
    FREE(0, 0, 1.0);

    private static final int DEFAULT_COST = 1250;
    private static final int DEFAULT_DISTANCE = 10;
    private int minAge;
    private int deductionCost;
    private double discountRate;

    CostType(int minAge, int deductionCost, double discountRate) {
        this.minAge = minAge;
        this.deductionCost = deductionCost;
        this.discountRate = discountRate;
    }

    public int getMinAge() {
        return minAge;
    }

    private int getMyCost(int additionalCost) {
        return DEFAULT_COST - deductionCost + additionalCost;
    }

    public int getFare(int distance) {
        int additionalCost = getAdditionalCost(distance);
        return (int) (getMyCost(additionalCost) - (getMyCost(additionalCost) * discountRate));
    }

    private int getAdditionalCost(int distance) {
        int additionalCost = 0;
        if (distance > DEFAULT_DISTANCE) {
            additionalCost = new AdditionalFareCalculator(distance - DEFAULT_DISTANCE).getAdditionalFare();
        }
        return additionalCost;
    }

    public static CostType getCostType(int age) {
        return stream(CostType.values()).filter(c -> age >= c.getMinAge())
                .findFirst()
                .orElse(FREE);
    }
}
