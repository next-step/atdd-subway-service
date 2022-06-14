package nextstep.subway.fare.domain.age;

import java.util.Arrays;
import nextstep.subway.exception.BadRequestException;
import nextstep.subway.exception.ExceptionType;
import nextstep.subway.fare.domain.age.impl.AdultFarePolicy;
import nextstep.subway.fare.domain.age.impl.ChildFarePolicy;
import nextstep.subway.fare.domain.age.impl.OldFarePolicy;
import nextstep.subway.fare.domain.age.impl.TeenagerFarePolicy;
import nextstep.subway.fare.domain.age.impl.ToddlerFarePolicy;

public enum AgeFareType {
    OLD(OldFarePolicy.getInstance()),
    ADULT(AdultFarePolicy.getInstance()),
    TEENAGER(TeenagerFarePolicy.getInstance()),
    CHILD(ChildFarePolicy.getInstance()),
    TODDLER(ToddlerFarePolicy.getInstance());

    private final AgePolicy agePolicy;

    AgeFareType(AgePolicy agePolicy) {
        this.agePolicy = agePolicy;
    }

    public static AgePolicy getAgePolicy(int age) {
        validateAge(age);
        return Arrays.stream(values())
            .filter(type -> type.agePolicy.includeAge(age))
            .findFirst()
            .orElseThrow(() -> new BadRequestException(ExceptionType.INVALID_AGE))
            .getAgePolicy();
    }

    private static void validateAge(int age) {
        if (age <= 0) {
            throw new BadRequestException(ExceptionType.INVALID_AGE.getMessage((long) age));
        }
    }

    public AgePolicy getAgePolicy() {
        return agePolicy;
    }
}
