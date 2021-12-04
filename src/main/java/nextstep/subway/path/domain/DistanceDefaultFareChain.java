package nextstep.subway.path.domain;

public class DistanceDefaultFareChain extends DistanceFareChain {

    public DistanceDefaultFareChain(DistanceFareChain chain) {
        super(chain);
    }

    @Override
    public int calculate(int distance) {
        return DEFAULT_OVER_FARE;
    }
}
