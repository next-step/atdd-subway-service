package nextstep.subway.fare.domain;

public class Fare {
    private final int BASIC_FARE = 1250;

    private int fare = BASIC_FARE;

    public Fare(int distance) {
        fare += calculateOverFare(distance);
    }

    private int calculateOverFare(int distance) {
        if(distance <= 10) {
            return 0;
        }
        if(distance <= 50){
            return (int) (Math.ceil((distance - 10) / 5) * 100);
        }
        return (int) (Math.ceil((distance - 10) / 8) * 100);
    }

    public int getFare() {
        return fare;
    }
}
