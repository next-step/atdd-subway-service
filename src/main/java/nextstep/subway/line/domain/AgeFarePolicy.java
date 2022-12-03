package nextstep.subway.line.domain;

import java.util.Arrays;
import nextstep.subway.member.domain.Age;

public enum AgeFarePolicy {

    ADULT(Age.from(19), 1, Fare.from(0)),
    TEENAGER(Age.from(13), 0.8, Fare.from(350)),
    CHILD(Age.from(6), 0.5, Fare.from(350)),
    BABY(Age.from(1), 0, Fare.from(0)),
    ;

    private final Age minAge;
    private final double rate;
    private final Fare deductionFare;

    AgeFarePolicy(Age minAge, double rate, Fare deductionFare) {
        this.minAge = minAge;
        this.rate = rate;
        this.deductionFare = deductionFare;
    }

    public static AgeFarePolicy findAgeFarePolicy(Age age) {
        return Arrays.stream(AgeFarePolicy.values())
                .filter(ageFarePolicy -> age.isEqualOrOlderThan(ageFarePolicy.minAge))
                .findFirst()
                .orElse(BABY);
    }

    public Fare calculateFare(Fare fare) {
        return fare.subtract(this.deductionFare)
                .multiplyAndCeil(rate);
    }

    public boolean isNotAdult() {
        return this != ADULT;
    }

    public Age getMinAge() {
        return minAge;
    }
}
