package nextstep.subway.fares.policy;

import nextstep.subway.path.domain.Distance;

public class DistanceBasedFarePolicyThird extends DistanceBasedFarePolicyDetails {
    private static final int PER_DISTANCE = 8;

    public DistanceBasedFarePolicyThird() {
        super(PER_DISTANCE);
    }

    @Override
    public boolean fareAddable(Distance currDistance, Distance targetDistance) {
        return currDistance.getValue() + PER_DISTANCE <= targetDistance.getValue();
    }
}
