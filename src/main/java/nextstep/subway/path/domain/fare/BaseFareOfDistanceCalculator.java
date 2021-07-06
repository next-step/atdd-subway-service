package nextstep.subway.path.domain.fare;

public class BaseFareOfDistanceCalculator implements FareOfDistanceCalculator {

    private static final int BASE_FARE = 1250;

    @Override
    public int calculate(int distance) {
        if(distance <= 0) {
            return 0;
        }
        return BASE_FARE;
    }
}
