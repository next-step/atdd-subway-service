package nextstep.subway.path.domain.distance_policy;

public class Over10KmDistanceFarePolicy extends DistanceFarePolicy {

    public Over10KmDistanceFarePolicy() {
    }

    @Override
    public int calculateByDistance() {
        distance = Math.min(50, distance) - 10;
        return BASIC_FARE + (int) ((Math.ceil((distance - 1) / 5) + 1) * 100);
    }
}
