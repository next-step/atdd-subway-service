package nextstep.subway.path.domain;

import nextstep.subway.path.domain.fare.FareCalculator;

public class Fare {
    private final int distance;
    private final int age;
    private final int maxAdditionalFare;
    private int result;

    public Fare(final int distance, final int age, final int maxAdditionalFare) {
        this.distance = distance;
        this.age = age;
        this.maxAdditionalFare = maxAdditionalFare;
    }

    public Fare acceptPolicy(FareCalculator fareCalculator) {
        this.result = fareCalculator.calculate(this);
        return this;
    }

    public int getDistance() {
        return distance;
    }

    public int getAge() {
        return age;
    }

    public int getMaxAdditionalFare() {
        return maxAdditionalFare;
    }

    public int getResult() {
        return result;
    }
}
