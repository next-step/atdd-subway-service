package nextstep.subway.path.domain;

public class LowOverFare implements OverFare {

    private final int LOW_DISTANCE = 40;
    private OverFare overFare;

    public LowOverFare(OverFare overFare) {
        this.overFare = overFare;
    }

    @Override
    public int calculate(int distance) {

        int fareDistance = distance;

        if(distance >= LOW_DISTANCE) {
            fareDistance = LOW_DISTANCE;
        }

        int fare = (int) ((Math.ceil((fareDistance - 1) / 5) + 1) * 100);
        return fare + overFare.calculate(distance - LOW_DISTANCE);
    }
}
