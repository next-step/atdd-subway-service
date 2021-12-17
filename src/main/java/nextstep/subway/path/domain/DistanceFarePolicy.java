package nextstep.subway.path.domain;

import java.util.Arrays;

public enum DistanceFarePolicy {
    BASE_RANGE(0, 10, 0), 
    FIRST_RANGE(11, 50, 5), 
    SECOND_RANGE(51, Integer.MAX_VALUE, 8);

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
        DistanceFarePolicy policy = judgePolicyGroup(distance);
        if (policy == FIRST_RANGE) {
            return DEFAULT_FARE + calculateOverFare(distance - BASE_RANGE.distanceRangeMax, policy.overPerDistance);
        }
        if (policy == SECOND_RANGE) {
            int firstRangeFare = calculateOverFare(FIRST_RANGE.distanceRangeMax - BASE_RANGE.distanceRangeMax, FIRST_RANGE.overPerDistance);
            int secondRangeFare = calculateOverFare(distance - FIRST_RANGE.distanceRangeMax, policy.overPerDistance);
            return DEFAULT_FARE + firstRangeFare + secondRangeFare;
        }
        
        return DEFAULT_FARE;
    }
    
    private static DistanceFarePolicy judgePolicyGroup(int distance) {
        return Arrays.stream(values())
                .filter(policy -> policy.judgeDistanceRange(distance))
                .findFirst()
                .orElse(BASE_RANGE);
    }
    
    private boolean judgeDistanceRange(int distance) {
        return distance >= this.distanceRangeMin && distance <= this.distanceRangeMax;
    }
    
    private static int calculateOverFare(int distance, int overDistance) {
        return (int) ((Math.ceil((distance - 1) / overDistance) + 1) * EXTRA_FARE);
    }

}
