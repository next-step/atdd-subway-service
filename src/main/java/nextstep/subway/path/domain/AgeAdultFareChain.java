package nextstep.subway.path.domain;

public class AgeAdultFareChain extends AgeFareChain {

    public AgeAdultFareChain(AgeFareChain chain) {
        super(chain);
    }

    @Override
    protected int calculate(int age) {
        if (age >= MIN_ADULT_AGE) {
            return ADULT_FARE;
        }
        return super.calculate(age);
    }
}
