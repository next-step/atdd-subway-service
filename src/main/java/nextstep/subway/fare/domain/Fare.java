package nextstep.subway.fare.domain;

public class Fare {
    public static final int BASIC_FARE = 1250;

    private int fare = BASIC_FARE;

    public Fare(int distance, int additionalFare) {
        fare += calculateOverFareByDistance(distance);
        fare += additionalFare;
    }

    private int calculateOverFareByDistance(int distance) {
        if(distance <= 10) {
            return 0;
        }
        if(distance <= 50){
            return (int) ((Math.ceil((distance - 1) / 5) + 1) * 100);
        }
        return (int) ((Math.ceil((distance - 1) / 8) + 1) * 100);
    }

    public int getFare() {
        return fare;
    }
}
