package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Sections;
import nextstep.subway.path.domain.policy.FarePolicy;

public class Fare {
    private int fare = 1_250;
    private FarePolicy farePolicy;

    public Fare(Sections sections, Path path, int age) {
        setFarePolicy(age);
        calculate(sections, path);
    }

    public static Fare of(Sections sections, Path path, int age) {
        return new Fare(sections, path, age);
    }

    private void calculate(Sections sections, Path path) {
        calculateLineExtraFare(sections, path);
        calculateOverFare(path.getDistance());
        calculateAgePolicy();
    }

    private void calculateLineExtraFare(Sections sections, Path path) {
        this.fare += path.getLineExtraFare(sections);
    }

    private void calculateOverFare(int distance) {
        if(distance >= 50) {
            this.fare += (int) (Math.floor(distance / 8.0) * 100);
        }
        if(distance > 10 && distance < 50) {
            this.fare += (int) (Math.floor(distance / 5.0) * 100);
        }
    }

    private void calculateAgePolicy() {
        this.fare = farePolicy.discount(this.fare);
    }

    private void setFarePolicy(int age) {
        this.farePolicy = AgeFarePolicy.findAgeFarePolicy(age);
    }

    public int getFare() {
        return fare;
    }
}
