package nextstep.subway.path.domain.distance;

public abstract class DistanceCalculate implements DistancePolicy {

    private final DistancePolicy policy;

    protected DistanceCalculate(DistancePolicy policy) {
        this.policy = policy;
    }

    protected int delegate() {
        return policy.calculate();
    }
}
