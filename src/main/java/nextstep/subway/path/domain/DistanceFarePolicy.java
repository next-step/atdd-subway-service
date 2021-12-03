package nextstep.subway.path.domain;

public class DistanceFarePolicy {

    private final DistanceFareChain chain;

    public DistanceFarePolicy() {
        this.chain = new Distance10FareChain(new Distance50FareChain(new DistanceDefaultFareChain(null)));
    }

    public int calculate(int distance) {
        return chain.calculate(distance);
    }
}
