package nextstep.subway.path.domain.policy.distance;

import nextstep.subway.path.domain.policy.FarePolicy;

import static nextstep.subway.path.domain.policy.distance.DistancePolicyFactory.FIFTY_DISTANCE;

public class OverFiftyPolicy implements FarePolicy {
    private static final int PER_FIVE_KILLO = 5;
    private static final int PER_EIGHT_KILLO = 8;
    private static final int PER_FIVE_KILL_SECTION_DISTANCE = 40;

    private final int distance;

    public OverFiftyPolicy(int distance) {
        this.distance = distance;
    }

    @Override
    public int calculate(int fare) {
        int overFiftyDistance = distance - FIFTY_DISTANCE;
        return fare + calculateOverFare(PER_FIVE_KILL_SECTION_DISTANCE, PER_FIVE_KILLO)
                + calculateOverFare(overFiftyDistance, PER_EIGHT_KILLO);
    }

    private int calculateOverFare(int distance, int perKillo) {
        return (int) ((Math.ceil((distance - 1) / perKillo) + 1) * 100);
    }
}
