package nextstep.subway.path.domain;

public enum FarePolicyByDistance {

    POLICY_UNDER_FIFTY(10, 5),
    POLICY_OVER_FIFTY(50, 8);

    private int minDistanceStandard;
    private int unitDistanceForCharge;

    FarePolicyByDistance(int minDistanceStandard, int unitDistanceForCharge) {
        this.minDistanceStandard = minDistanceStandard;
        this.unitDistanceForCharge = unitDistanceForCharge;
    }

    public int getMinDistanceStandard() {
        return minDistanceStandard;
    }

    public int getUnitDistanceForCharge() {
        return unitDistanceForCharge;
    }
}
