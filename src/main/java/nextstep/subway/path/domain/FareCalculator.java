package nextstep.subway.path.domain;

public class FareCalculator {
    private static final int DEFAULT_FARE = 1_250;
    private static final int OVER_DISTANCE_MIN = 10;
    private static final int OVER_DISTANCE_MAX = 50;
    private static final int OVER_PER_DISTANCE_KM = 5;
    private static final int MAX_OVER_PER_DISTANCE_KM = 8;
    private static final int EXTRA_FARE = 100;
    
    private FareCalculator() {
    }
    
    public static int calculator(int distance) {
        if (distance <= OVER_DISTANCE_MIN) {
            return DEFAULT_FARE;
        }
        if (distance > OVER_DISTANCE_MIN && distance <= OVER_DISTANCE_MAX) {
            return calculateOverFare(distance, OVER_PER_DISTANCE_KM);
        }
        if (distance > OVER_DISTANCE_MAX) {
            return calculateOverFare(distance, MAX_OVER_PER_DISTANCE_KM);
        }
        
        return 0;
    }
    
    private static int calculateOverFare(int distance, int overDistance) {
        return (int) ((Math.ceil((distance - 1) / overDistance) + 1) * EXTRA_FARE);
    }
}
