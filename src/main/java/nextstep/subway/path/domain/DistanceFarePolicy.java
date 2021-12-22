package nextstep.subway.path.domain;

public enum DistanceFarePolicy {
    FIRST_RANGE(10, 50, 5), 
    SECOND_RANGE(50, Integer.MAX_VALUE, 8);

    public static final int DEFAULT_FARE = 1_250;
    public static final int EXTRA_FARE = 100;
    
    public final int distanceRangeMin;
    public final int distanceRangeMax;
    public final int overPerDistance;

    DistanceFarePolicy(int distanceRangeMin, int distanceRangeMax, int overPerDistance) {
        this.distanceRangeMin = distanceRangeMin;
        this.distanceRangeMax = distanceRangeMax;
        this.overPerDistance = overPerDistance;
    }

}
