package nextstep.subway.path.domain.decorator;

public class MiddleDistancePolicy extends DistanceDecorator {
    private static final int CONDITION = 5;

    private final int distance;

    public MiddleDistancePolicy(DistancePolicy policy, int distance) {
        super(policy);
        this.distance = distance;
    }

    @Override
    public int calculate() {
        return super.delegate() + calculateExtra();
    }

    private int calculateExtra() {
        return (int) ((Math.ceil((distance - 1) / CONDITION) + 1) * INCREMENT_FARE);
    }
}
