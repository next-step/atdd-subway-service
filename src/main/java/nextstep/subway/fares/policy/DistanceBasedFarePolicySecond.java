package nextstep.subway.fares.policy;

import nextstep.subway.path.domain.Distance;

public class DistanceBasedFarePolicySecond extends DistanceBasedFarePolicyDetails {
    private static final int PER_DISTANCE = 5;
    private static final int MAX_DISTANCE = 50;

    public DistanceBasedFarePolicySecond() {
        super(PER_DISTANCE);
    }

    @Override
    public boolean fareAddable(Distance currDistance, Distance targetDistance) {
        return currDistance.getValue() < MAX_DISTANCE
                && currDistance.getValue() + PER_DISTANCE <= targetDistance.getValue();
    }
}
