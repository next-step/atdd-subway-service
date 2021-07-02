package nextstep.subway.path.domain.fare;

public class BaseFareCalculator implements FareCalculator {

    private static final int BASE_RATE = 1250;

    @Override
    public int calculate(int distance) {
        if(distance <= 0) {
            return 0;
        }
        return BASE_RATE;
    }
}
