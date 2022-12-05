package nextstep.subway.path.domain.distance;

import nextstep.subway.line.domain.ExtraFare;

public class MiddleDistancePolicy extends DistanceCalculate {
    private static final int CONDITION = 5;

    private final int distance;

    public MiddleDistancePolicy(DistancePolicy policy, int distance) {
        super(policy);
        this.distance = distance;
    }

    private int calculateExtra() {
        return (int) ((Math.ceil((distance - 1) / CONDITION) + 1) * ExtraFare.INCREMENT);
    }

    @Override
    public int calculate() {
        return super.delegate() + calculateExtra();
    }
}

