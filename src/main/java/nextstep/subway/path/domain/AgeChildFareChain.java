package nextstep.subway.path.domain;

public class AgeChildFareChain extends AgeFareChain {

    public AgeChildFareChain(AgeFareChain chain) {
        super(chain);
    }

    @Override
    protected int calculate(int age) {
        if (age >= MIN_CHILD_AGE && age < MIN_YOUTH_AGE) {
            return (int) ((ADULT_FARE - DISCOUNT_FARE) * CHILD_DISCOUNT_RATE);
        }
        return super.calculate(age);
    }
}
