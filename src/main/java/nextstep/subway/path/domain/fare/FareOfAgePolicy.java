package nextstep.subway.path.domain.fare;

import nextstep.subway.path.domain.Fare;

public class FareOfAgePolicy implements FareCalculator {

    private final int age;

    public FareOfAgePolicy(final int age) {
        this.age = age;
    }

    @Override
    public int calculate(Fare fare) {
        return FareOfAgePolicyFactory.discount(age, fare.getResult());
    }
}
