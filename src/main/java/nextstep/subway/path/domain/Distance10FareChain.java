package nextstep.subway.path.domain;

public class Distance10FareChain extends DistanceFareChain {

    public Distance10FareChain(DistanceFareChain chain) {
        super(chain);
    }

    @Override
    public int calculate(int distance) {
        if (distance > SHORT_DISTANCE && distance <= LONG_DISTANCE) {
            return calculateOverFare(distance - SHORT_DISTANCE, UNIT_SHORT_DISTANCE);
        }

        return super.calculate(distance);
    }
}
