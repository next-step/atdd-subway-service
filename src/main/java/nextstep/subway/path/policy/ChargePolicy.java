package nextstep.subway.path.policy;

import java.util.Arrays;

public enum ChargePolicy {
    BASIC_DISTANCE(0, 10, 1250, 0, 0),
    MIDDLE_DISTANCE(10, 50, 1250, 5, 100),
    LONG_DISTANCE(50, Integer.MAX_VALUE, 1250, 8, 100);

    private int minDistance;
    private int maxDistance;
    private int charge;
    private int surchargeDistance;
    private int surcharge;

    ChargePolicy(int minDistance, int maxDistance, int charge, int surchargeDistance, int surcharge) {
        this.minDistance = minDistance;
        this.maxDistance = maxDistance;
        this.charge = charge;
        this.surchargeDistance = surchargeDistance;
        this.surcharge = surcharge;
    }

    public static ChargePolicy getDistancePolicy(int distance) {
        return Arrays.stream(ChargePolicy.values())
                .filter(chargePolicy -> chargePolicy.minDistance < distance && distance <= chargePolicy.maxDistance)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("잘 못 된 거리입니다."));
    }

    public int chargeCalculate(int distance) {
        return this.charge +
                (int) ((Math.ceil((distance - this.minDistance - 1 ) / this.surchargeDistance)) + 1) * this.surcharge;
    }
}
