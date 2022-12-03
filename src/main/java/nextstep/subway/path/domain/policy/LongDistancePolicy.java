package nextstep.subway.path.domain.policy;

import nextstep.subway.line.domain.ExtraFare;

public class LongDistancePolicy extends DistanceDecorator {
    private static final int CONDITION = 8;
    private final int distance;

    public LongDistancePolicy(DistancePolicy policy, int distance) {
        super(policy);
        this.distance = distance;
    }

    @Override
    public int calculate() {
        return super.delegate() + calculateExtra();
    }

    private int calculateExtra() {
        return (int) ((Math.ceil((distance - 1) / CONDITION) + 1) * ExtraFare.INCREMENT);
    }
}
