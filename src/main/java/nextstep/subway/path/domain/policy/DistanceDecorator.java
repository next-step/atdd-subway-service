package nextstep.subway.path.domain.policy;

public abstract class DistanceDecorator implements DistancePolicy {

    private final DistancePolicy policy;

    protected DistanceDecorator(DistancePolicy policy) {
        this.policy = policy;
    }

    protected int delegate() {
        return policy.calculate();
    }
}
