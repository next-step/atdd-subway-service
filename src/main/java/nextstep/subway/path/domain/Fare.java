package nextstep.subway.path.domain;

public class Fare {

    private static final int BASIC_FARE = 1250;
    private static final int BASIC_DISTANCE = 10;


    private int fare;

    public Fare() {
        fare = BASIC_FARE;
    }

    public static Fare of(int distance, int additionalFare) {
        Fare fare = new Fare();
        fare.calculateFare(distance, additionalFare);
        return fare;
    }

    private void calculateFare(int distance, int additionalFare) {
        calculateDistanceFare(distance);
        addAdditionalFare(additionalFare);
    }

    private void calculateDistanceFare(int distance) {
        if (distance > BASIC_DISTANCE) {
            DistanceFare distanceFare = DistanceFare.of(distance);
            fare += distanceFare.calculateOverFare(distance);
        }
    }

    private void addAdditionalFare(int additionalFare) {
        fare += additionalFare;
    }

    public int getFare() {
        return fare;
    }
}
