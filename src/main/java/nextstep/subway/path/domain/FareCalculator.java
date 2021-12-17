package nextstep.subway.path.domain;

import nextstep.subway.auth.domain.Age;
import nextstep.subway.line.domain.Fare;

public class FareCalculator {
    private final Path path;
    private final Age age;
    public FareCalculator(Path path, Age age) {
        this.path = path;
        this.age = age;
    }

    public int calculate() {
        Fare fare = DistanceFarePolicy.calculate(path.getDistance(), path.getExtraFare());
        fare = AgeFarePolicy.discount(age.getValue(), fare);
        return fare.getValue();
    }
}
