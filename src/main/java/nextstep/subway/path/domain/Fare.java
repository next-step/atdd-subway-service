package nextstep.subway.path.domain;

public class Fare {
    private int value;

    public Fare(int extraFare, int distance, int age) {
        int fare = extraFare + FareDistance.calculate(distance);
        this.value =  fare - AgeDiscount.calculate(age, fare);
    }

    public int get() {
        return value;
    }
}
