package nextstep.subway.path.domain;

import nextstep.subway.auth.domain.Age;

public class FareCalculator {
    private final Path path;
    private final Age age;
    public FareCalculator(Path path, Age age) {
        this.path = path;
        this.age = age;
    }

    public int calculate() {
        int fare = DistanceFarePolicy.calculate(path.getDistance(), path.getExtraFare());
        return discountApply(fare);
    }

    private int discountApply(int fare) {
        if(age.isChild()) {
            return new ChildDiscountPolicy().apply(fare);
        }
        if(age.isTeenager()) {
            return new TeenagerDiscountPolicy().apply(fare);
        }
        return fare;
    }
}
