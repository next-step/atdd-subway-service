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
    OLD(age -> age >= 65, 100, OldFarePolicy.getInstance()),
    ADULT(age -> age >= 19, 100, AdultFarePolicy.getInstance()),
    TEENAGER(age -> age >= 13 && age < 19, 80, TeenagerFarePolicy.getInstance()),
    CHILD(age -> age >= 6 && age < 13, 50, ChildFarePolicy.getInstance()),
    TODDLER(age -> age < 6, 100, ToddlerFarePolicy.getInstance());

    private final AgePredicate predicate;
    private final int discountPercent;
    private final AgePolicy agePolicy;

    AgeFareType(AgePredicate predicate, int discountPercent, AgePolicy agePolicy) {
        this.predicate = predicate;
        this.discountPercent = discountPercent;
        this.agePolicy = agePolicy;
    }

    public static AgePolicy getAgePolicy(int age) {
        validateAge(age);
        return Arrays.stream(values())
            .filter(type -> type.predicate.includeAge(age))
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

    public int getDiscountPercent() {
        return discountPercent;
    }
}
