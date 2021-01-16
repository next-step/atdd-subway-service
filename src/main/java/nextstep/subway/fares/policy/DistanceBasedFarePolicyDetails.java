package nextstep.subway.fares.policy;

import nextstep.subway.fares.domain.Fare;
import nextstep.subway.path.domain.Distance;

public abstract class DistanceBasedFarePolicyDetails {
    private static final int DEFAULT_SURCHARGE = 100;

    private final int perDistance;

    public DistanceBasedFarePolicyDetails(int perDistance) {
        this.perDistance = perDistance;
    }

    public void calculateFareByDistance(Distance currDistance, Fare fare, Distance targetDistance) {
        while (fareAddable(currDistance, targetDistance)) {
            fare.add(DEFAULT_SURCHARGE);
            currDistance.add(perDistance);
        }
    }

    abstract public boolean fareAddable(Distance currDistance, Distance targetDistance);
}
