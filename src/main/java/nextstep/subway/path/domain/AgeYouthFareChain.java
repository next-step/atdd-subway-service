package nextstep.subway.path.domain;

public class AgeYouthFareChain extends AgeFareChain {

    public AgeYouthFareChain(AgeFareChain chain) {
        super(chain);
    }

    @Override
    protected int calculate(int age) {
        if (age >= MIN_YOUTH_AGE && age < MIN_ADULT_AGE) {
            return (int) ((ADULT_FARE - DISCOUNT_FARE) * YOUTH_DISCOUNT_RATE);
        }
        return super.calculate(age);
    }
}
