package nextstep.subway.path.domain;

public class AgeDefaultFareChain extends AgeFareChain {

    public AgeDefaultFareChain(AgeFareChain chain) {
        super(chain);
    }

    @Override
    protected int calculate(int age) {
        return MIN_FARE;
    }
}
