package nextstep.subway.fares.policy;

import nextstep.subway.fares.domain.Fare;
import nextstep.subway.path.domain.Distance;

public class DistanceBasedFarePolicyFirst extends DistanceBasedFarePolicyDetails {
    private static final int PER_DISTANCE = 10;
    private static final int BASE_FARE = 1250;

    public DistanceBasedFarePolicyFirst() {
        super(0);
    }

    @Override
    public void calculateFareByDistance(Distance currDistance, Fare fare, Distance targetDistance) {
        currDistance.add(PER_DISTANCE);
        fare.add(BASE_FARE);
    }

    @Override
    public boolean fareAddable(Distance currDistance, Distance targetDistance) {
        return true;
    }
}
