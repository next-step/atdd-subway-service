package nextstep.subway.path.domain.fare.distance;

public class FinalDistanceFare extends DistanceFare {
    private static final int EIGHT_KM_DISTANCE = 8;
    private static final int FIFTY_KM_DISTANCE = 50;
    private int distance;

    public FinalDistanceFare(int distance) {
        this.distance = distance;
    }

    @Override
    public int calculateFare() {
        int fare = new MiddleDistanceFare(FIFTY_KM_DISTANCE).calculateFare();
        fare += calculateOverFare(distance - FIFTY_KM_DISTANCE, EIGHT_KM_DISTANCE);
        return fare;
    }
}
