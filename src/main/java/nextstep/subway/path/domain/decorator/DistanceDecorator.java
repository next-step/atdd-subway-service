package nextstep.subway.path.domain.decorator;

public abstract class DistanceDecorator implements DistancePolicy {
    protected static final int INCREMENT_FARE = 100;

    private final DistancePolicy policy;

    protected DistanceDecorator(DistancePolicy policy) {
        this.policy = policy;
    }

    protected int delegate() {
        return policy.calculate();
    }
}
