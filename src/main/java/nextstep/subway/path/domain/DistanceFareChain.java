package nextstep.subway.path.domain;

public class DistanceFareChain {

    protected static final int DEFAULT_OVER_FARE = 0;
    protected static final int SHORT_DISTANCE = 10;
    protected static final int LONG_DISTANCE = 50;
    protected static final int UNIT_SHORT_DISTANCE = 5;
    protected static final int UNIT_LONG_DISTANCE = 8;
    protected static final int UNIT_FARE = 100;

    private final DistanceFareChain chain;

    public DistanceFareChain(DistanceFareChain chain) {
        this.chain = chain;
    }

    public int calculate(int distance) {
        return chain.calculate(distance);
    }

    protected int calculateOverFare(int distance, int unitDistance) {
        return (int) ((Math.ceil(((distance) - 1) / unitDistance) + 1) * UNIT_FARE);
    }
}
