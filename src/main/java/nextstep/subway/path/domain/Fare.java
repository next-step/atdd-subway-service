package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Sections;

public class Fare {
    private int fare = 1_250;

    public Fare(Sections sections, Path path, int age) {
        calculate(sections, path, age);
    }

    public static Fare of(Sections sections, Path path, int age) {
        return new Fare(sections, path, age);
    }

    private void calculate(Sections sections, Path path, int age) {
        calculateLineExtraFare(sections, path);
        calculateOverFare(path.getDistance());
        calculateAgePolicy(age);
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

    private void calculateAgePolicy(int age) {
        this.fare = AgeFarePolicy.calculate(this.fare, age);
    }

    public int getFare() {
        return fare;
    }
}
