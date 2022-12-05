package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Fare;

public class FareAgeCalculator {

    public static Fare calculate(int fare, int age) {
        FareAge fareAge = FareAge.findByAge(age);
        return Fare.from(fareAge.calculateFare(fare));
    }
}
