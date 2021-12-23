package nextstep.subway.path.domain;

import java.util.Arrays;
import java.util.Optional;

public class DistanceFareCalculator {
    
    public static int calculator(int distance) {
        Optional<DistanceFarePolicy> distancePolicy = judgePolicyGroup(distance);
        int fare = DistanceFarePolicy.DEFAULT_FARE;
        if (!distancePolicy.isPresent()) {
            return fare;
        }
        
        for (int i = 0; i <= distancePolicy.get().ordinal(); i++) {
            fare += getCalculateFare(DistanceFarePolicy.values()[i], distance);
        }
        
        return fare;
    }
    
    private static Optional<DistanceFarePolicy> judgePolicyGroup(int distance) {
        return Arrays.stream(DistanceFarePolicy.values())
                .filter(policy -> judgeDistanceRange(policy, distance))
                .findFirst();
    }
    
    private static boolean judgeDistanceRange(DistanceFarePolicy policy, int distance) {
        return distance > policy.distanceRangeMin && distance <= policy.distanceRangeMax;
    }
    
    private static int calculateOverFare(int distance, int overDistance, int extraFare) {
        return (int) ((Math.ceil((distance - 1) / overDistance) + 1) * extraFare);
    }
    
    private static int getCalculateFare(DistanceFarePolicy policy, int distance) {
        if (policy.distanceRangeMax >= distance) {
            return calculateOverFare(distance - policy.distanceRangeMin, policy.overPerDistance, policy.EXTRA_FARE);
        }
        return calculateOverFare(policy.distanceRangeMax - policy.distanceRangeMin, policy.overPerDistance, policy.EXTRA_FARE);
    }
    

}
