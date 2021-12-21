package nextstep.subway.path.domain;

public class Fare {
    private int fare;

    private Fare() {
    }

    private Fare(int fare) {
        this.fare = fare;
    }

    public static Fare of(int distance, int surcharge, int age) {
        return new Fare(FareCalculator.calculator(distance, surcharge, age));
    }

    public int getFare() {
        return fare;
    }
}
