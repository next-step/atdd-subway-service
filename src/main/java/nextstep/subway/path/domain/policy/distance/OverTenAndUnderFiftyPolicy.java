package nextstep.subway.path.domain.policy.distance;

import nextstep.subway.path.domain.policy.FarePolicy;

import static nextstep.subway.path.domain.policy.distance.DistancePolicyFactory.TEN_DISTANCE;

public class OverTenAndUnderFiftyPolicy implements FarePolicy {
    private static final int PER_FIVE_KILLO = 5;

    private final int distance;

    public OverTenAndUnderFiftyPolicy(int distance) {
        this.distance = distance;
    }

    @Override
    public int calculate(int fare) {
        return fare + calculateOverFare(distance - TEN_DISTANCE);
    }

    private int calculateOverFare(int distance) {
        return (int) ((Math.ceil((distance - 1) / PER_FIVE_KILLO) + 1) * 100);
    }
}
