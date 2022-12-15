package nextstep.subway.fare.domain;

import nextstep.subway.line.domain.Distance;

import java.util.Arrays;

public enum DistanceFarePolicy {
    NONE(1, 10, 0, 0),
    OVER_TEN(11, 50, 5, 100),
    OVER_FIFTY(51, Integer.MAX_VALUE, 8, 100),
    ;

    private final int startDistance;
    private final int endDistance;
    private final int additionalDistanceSection;
    private final int additionalFare;
    private final Fare maxFare;

    DistanceFarePolicy(int startDistance,
                       int endDistance,
                       int additionalDistanceSection,
                       int additionalFare) {
        this.startDistance = startDistance;
        this.endDistance = endDistance;
        this.additionalDistanceSection = additionalDistanceSection;
        this.additionalFare = additionalFare;
        this.maxFare = calculateMaxFare(endDistance);
    }

    public static DistanceFarePolicy of(Distance distance) {
        return Arrays.stream(values())
                .filter(policy -> policy.isAssignable(distance))
                .findFirst()
                .orElseThrow(IllegalAccessError::new);
    }

    public Fare apply(Distance distance) {
        if(isNone()) {
            return Fare.zero();
        }
        Fare previousPolicyFare = sumPreviousPolicyFare();
        return previousPolicyFare.plus(calculateFare(distance));
    }

    private Fare calculateMaxFare(int distance) {
        if(!hasNextPolicy()) {
            return Fare.zero();
        }
        return calculateFare(new Distance(distance));
    }

    private boolean hasNextPolicy() {
        return this.endDistance != Integer.MAX_VALUE;
    }

    private Fare calculateFare(Distance distance) {
        if(isNone()) {
            return Fare.zero();
        }

        int additionalDistance = distance.value() - (startDistance - 1);
        int overFare = (int) (Math.ceil((additionalDistance - 1) / additionalDistanceSection) + 1) * additionalFare;
        return Fare.of(overFare);
    }

    private boolean isAssignable(Distance distance) {
        return this.startDistance <= distance.value()
                && distance.value() <= this.endDistance;
    }

    private boolean isNone() {
        return additionalDistanceSection < 1 || DistanceFarePolicy.NONE.equals(this);
    }

    private Fare sumPreviousPolicyFare() {
        return Arrays.stream(values())
                .filter(policy -> policy.isPreviousPolicy(this))
                .map(previousPolicy -> previousPolicy.maxFare)
                .reduce(Fare::plus)
                .orElse(Fare.zero());
    }

    private boolean isPreviousPolicy(DistanceFarePolicy distanceFarePolicy) {
        return this.endDistance < distanceFarePolicy.startDistance;
    }
}
