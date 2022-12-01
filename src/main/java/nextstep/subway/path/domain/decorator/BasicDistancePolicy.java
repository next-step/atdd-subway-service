package nextstep.subway.path.domain.decorator;

public class BasicDistancePolicy implements DistancePolicy {
    private static final int BASIC_FARE = 1_250;

    @Override
    public int calculate() {
        return BASIC_FARE;
    }
}
