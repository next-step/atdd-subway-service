package nextstep.subway.path.domain.policy.distance;

import nextstep.subway.path.domain.policy.FarePolicy;

import static nextstep.subway.path.domain.policy.distance.DistancePolicyFactory.TEN_DISTANCE;

public class OverTenAndUnderFiftyPolicy implements FarePolicy {
    private final int distance;

    public OverTenAndUnderFiftyPolicy(int distance) {
        this.distance = distance;
    }

    @Override
    public int calculate(int fare) {
        return fare + calculateOverFarePerFiveKillo(distance - TEN_DISTANCE);
    }

    private int calculateOverFarePerFiveKillo(int distance) {
        return (int) ((Math.ceil((distance - 1) / 5) + 1) * 100);
    }
}
