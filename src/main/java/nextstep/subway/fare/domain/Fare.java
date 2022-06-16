package nextstep.subway.fare.domain;

import java.util.Objects;
import nextstep.subway.fare.domain.policy.age.AgeFarePolicy;
import nextstep.subway.fare.domain.policy.age.AgeFarePolicyType;
import nextstep.subway.fare.domain.policy.age.impl.FreeAgeFarePolicy;
import nextstep.subway.fare.domain.policy.distance.DistanceFarePolicy;
import nextstep.subway.fare.domain.policy.distance.DistanceFarePolicyType;

public class Fare {

    private final int value;

    public Fare(int distance, int additionalLineFare, int age) {
        DistanceFarePolicy distanceFarePolicy = DistanceFarePolicyType.getDistanceFarePolicy(distance);
        AgeFarePolicy ageFarePolicy = AgeFarePolicyType.getAgeFarePolicy(age);

        int defaultAgeFare = ageFarePolicy.calculate();
        int additionalDistanceFare = distanceFarePolicy.calculate(distance, ageFarePolicy);
        int lineFare = ageFarePolicy instanceof FreeAgeFarePolicy ? 0 : additionalLineFare;

        this.value = defaultAgeFare + additionalDistanceFare + lineFare;
    }

    public int getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Fare fare = (Fare) o;
        return value == fare.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

}
