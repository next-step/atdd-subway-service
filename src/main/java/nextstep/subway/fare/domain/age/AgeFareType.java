package nextstep.subway.fare.domain.age;

import java.util.Arrays;
import java.util.function.Predicate;
import nextstep.subway.exception.BadRequestException;
import nextstep.subway.exception.ExceptionType;
import nextstep.subway.fare.domain.age.impl.AdultFarePolicy;
import nextstep.subway.fare.domain.age.impl.ChildFarePolicy;
import nextstep.subway.fare.domain.age.impl.OldFarePolicy;
import nextstep.subway.fare.domain.age.impl.TeenagerFarePolicy;
import nextstep.subway.fare.domain.age.impl.ToddlerFarePolicy;

public enum AgeFareType {
    OLD(age -> age >= 65, OldFarePolicy.getInstance()),
    ADULT(age -> age >= 19, AdultFarePolicy.getInstance()),
    TEENAGER(age -> age >= 13 && age < 19, TeenagerFarePolicy.getInstance()),
    CHILD(age -> age >= 6 && age < 13, ChildFarePolicy.getInstance()),
    TODDLER(age -> age < 6, ToddlerFarePolicy.getInstance());

    private final Predicate<Integer> predicate;
    private final AgePolicy agePolicy;

    AgeFareType(Predicate<Integer> predicate, AgePolicy agePolicy) {
        this.predicate = predicate;
        this.agePolicy = agePolicy;
    }

    public static AgePolicy getAgePolicy(int age) {
        validateAge(age);
        return Arrays.stream(values())
            .filter(type -> type.predicate.test(age))
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
