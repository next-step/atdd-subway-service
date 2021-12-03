package nextstep.subway.path.domain;

public class Distance50FareChain extends DistanceFareChain {

    public Distance50FareChain(DistanceFareChain chain) {
        super(chain);
    }

    @Override
    public int calculate(int distance) {
        if (distance > LONG_DISTANCE) {
            return calculateOverFare(distance - LONG_DISTANCE, UNIT_LONG_DISTANCE) + calculateOverFare(LONG_DISTANCE - SHORT_DISTANCE, UNIT_SHORT_DISTANCE);
        }
        return super.calculate(distance);
    }
}
