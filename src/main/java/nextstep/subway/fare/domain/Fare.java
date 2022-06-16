package nextstep.subway.fare.domain;

import java.util.Objects;
import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.fare.domain.policy.age.AgeFarePolicy;
import nextstep.subway.fare.domain.policy.age.AgeFarePolicyType;
import nextstep.subway.fare.domain.policy.age.impl.FreeAgeFarePolicy;
import nextstep.subway.fare.domain.policy.distance.DistanceFarePolicy;
import nextstep.subway.fare.domain.policy.distance.DistanceFarePolicyType;
import nextstep.subway.path.domain.Path;

public class Fare {

    private final int value;

    public Fare(Path path, LoginMember loginMember) {
        int distance = path.getDistance();
        int age = loginMember.getAge();

        DistanceFarePolicy distanceFarePolicy = DistanceFarePolicyType.getDistanceFarePolicy(distance);
        AgeFarePolicy ageFarePolicy = AgeFarePolicyType.getAgeFarePolicy(age);

        int defaultAgeFare = ageFarePolicy.calculate(age);
        int additionalDistanceFare = distanceFarePolicy.calculate(distance, ageFarePolicy);
        int additionalLineFare = ageFarePolicy instanceof FreeAgeFarePolicy ? 0 : path.additionalLineFare();

        this.value = defaultAgeFare + additionalDistanceFare + additionalLineFare;
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
