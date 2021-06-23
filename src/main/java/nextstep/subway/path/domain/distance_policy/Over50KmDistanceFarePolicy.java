package nextstep.subway.path.domain.distance_policy;

public class Over50KmDistanceFarePolicy extends DistanceFarePolicy {

    private static final int MIN_DISTANCE = 50;
    private static final int ADDITIONAL_FARE_UNTIL_50KM = 800;

    public Over50KmDistanceFarePolicy(int distance) {
        super(distance);
    }

    @Override
    public int calculateByDistance() {
        distance = distance - MIN_DISTANCE;
        return BASIC_FARE + ADDITIONAL_FARE_UNTIL_50KM + (int) ((Math.ceil((distance - 1) / 8) + 1) * 100);
    }
}
