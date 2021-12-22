package nextstep.subway.path.domain;

import java.util.Arrays;
import java.util.Optional;

public enum DistanceFarePolicy {
    FIRST_RANGE(10, 50, 5), 
    SECOND_RANGE(50, Integer.MAX_VALUE, 8);

    private static final int DEFAULT_FARE = 1_250;
    private static final int EXTRA_FARE = 100;
    
    private final int distanceRangeMin;
    private final int distanceRangeMax;
    private final int overPerDistance;

    DistanceFarePolicy(int distanceRangeMin, int distanceRangeMax, int overPerDistance) {
        this.distanceRangeMin = distanceRangeMin;
        this.distanceRangeMax = distanceRangeMax;
        this.overPerDistance = overPerDistance;
    }
    
    public static int calculator(int distance) {
        Optional<DistanceFarePolicy> distancePolicy = judgePolicyGroup(distance);
        int fare = DEFAULT_FARE;
        if (!distancePolicy.isPresent()) {
            return fare;
        }
        for (int i = 0; i <= distancePolicy.get().ordinal(); i++) {
            DistanceFarePolicy policy = values()[i];
            fare += getCalculateFare(policy, distance);
        }
        return fare;
    }
    
    private static Optional<DistanceFarePolicy> judgePolicyGroup(int distance) {
        return Arrays.stream(values())
                .filter(policy -> policy.judgeDistanceRange(distance))
                .findFirst();
    }
    
    private boolean judgeDistanceRange(int distance) {
        return distance > this.distanceRangeMin && distance <= this.distanceRangeMax;
    }
    
    private static int calculateOverFare(int distance, int overDistance) {
        return (int) ((Math.ceil((distance - 1) / overDistance) + 1) * EXTRA_FARE);
    }
    
    private static int getCalculateFare(DistanceFarePolicy policy, int distance) {
        if (policy.distanceRangeMax >= distance) {
            return calculateOverFare(distance - policy.distanceRangeMin, policy.overPerDistance);
        }
        return calculateOverFare(policy.distanceRangeMax - policy.distanceRangeMin, policy.overPerDistance);
    }
    

}
