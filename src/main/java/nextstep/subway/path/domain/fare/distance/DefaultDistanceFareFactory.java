package nextstep.subway.path.domain.fare.distance;

public class DefaultDistanceFareFactory implements DistanceFareFactory {
    private static final int TEN_KM_DISTANCE = 10;
    private static final int FIFTY_KM_DISTANCE = 50;

    @Override
    public DistanceFare getDistanceFare(int distance) {
        if (FIFTY_KM_DISTANCE < distance) {
            return new FinalDistanceFare(distance);
        } else if (TEN_KM_DISTANCE < distance) {
            return new MiddleDistanceFare(distance);
        }
        return new BasicDistanceFare(distance);
    }
}
