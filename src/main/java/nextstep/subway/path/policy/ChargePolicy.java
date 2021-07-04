package nextstep.subway.path.policy;

import java.util.Arrays;

public enum ChargePolicy {
    BASIC_DISTANCE(0, 10, 1250, 0, 0),
    MIDDLE_DISTANCE(10, 50, 1250, 5, 100),
    LONG_DISTANCE(50, Integer.MAX_VALUE, 1250, 8, 100);

    private int minDistance;
    private int maxDistance;
    private int charge;
    private int addChargeDistance;
    private int addCharge;

    ChargePolicy(int minDistance, int maxDistance, int charge, int addChargeDistance, int addCharge) {
        this.minDistance = minDistance;
        this.maxDistance = maxDistance;
        this.charge = charge;
        this.addChargeDistance = addChargeDistance;
        this.addCharge = addCharge;
    }

    public int getMinDistance() {
        return minDistance;
    }

    public int getMaxDistance() {
        return maxDistance;
    }

    public int getCharge() {
        return charge;
    }

    public int getAddChargeDistance() {
        return addChargeDistance;
    }

    public int getAddCharge() {
        return addCharge;
    }

    public static ChargePolicy getDistancePolicy(int distance) {
        return Arrays.stream(ChargePolicy.values())
                .filter(chargePolicy -> chargePolicy.minDistance < distance && distance <= chargePolicy.maxDistance)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("잘 못 된 거리입니다."));
    }
}
