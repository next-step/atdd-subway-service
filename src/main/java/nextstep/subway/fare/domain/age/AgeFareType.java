package nextstep.subway.fare.domain.age;

import static nextstep.subway.fare.domain.age.AgePolicy.DEFAULT_FARE;
import static nextstep.subway.fare.domain.age.AgePolicy.DISCOUNT_FARE;

import java.util.Arrays;
import java.util.function.Function;
import nextstep.subway.exception.BadRequestException;
import nextstep.subway.exception.ExceptionType;
import nextstep.subway.fare.domain.Fare;

public enum AgeFareType {
    OLD(age -> age >= 65, fare -> new Fare(0)),
    ADULT(age -> age >= 19 && age < 65, fare -> new Fare(fare.getValue() + DEFAULT_FARE)),
    TEENAGER(age -> age >= 13 && age <= 18, fare -> {
        int fareByAge = (int) ((DEFAULT_FARE - DISCOUNT_FARE) * 0.8);
        int discountFare = (int) ((fare.getValue()) * 0.8);
        return new Fare(fareByAge + discountFare);
    }),
    CHILD(age -> age >= 6 && age < 13, fare -> {
        int fareByAge = (int) ((DEFAULT_FARE - DISCOUNT_FARE) * 0.5);
        int discountFare = (int) ((fare.getValue()) * 0.5);
        return new Fare(fareByAge + discountFare);
    }),
    TODDLER(age -> 6 > age, fare -> new Fare(0));

    private final AgePredicate agePredicate;
    private final Function<Fare, Fare> calculateTotal;

    AgeFareType(AgePredicate agePredicate, Function<Fare, Fare> calculateTotal) {
        this.agePredicate = agePredicate;
        this.calculateTotal = calculateTotal;
    }

    public static Fare calculateTotalFare(int age, Fare fare) {
        validateAge(age);
        return Arrays.stream(values())
            .filter(type -> type.agePredicate.includeAge(age))
            .findFirst()
            .orElseThrow(() -> new BadRequestException(ExceptionType.INVALID_AGE))
            .calculateTotal.apply(fare);
    }

    private static void validateAge(int age) {
        if (age <= 0) {
            throw new BadRequestException(ExceptionType.INVALID_AGE.getMessage((long) age));
        }
    }
}
