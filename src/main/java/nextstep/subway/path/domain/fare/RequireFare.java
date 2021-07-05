package nextstep.subway.path.domain.fare;

import nextstep.subway.path.domain.Path;
import nextstep.subway.path.domain.fare.discount.Discount;

import java.util.Arrays;
import java.util.function.BiFunction;
import java.util.function.Predicate;

import static nextstep.subway.path.domain.fare.discount.AgeDiscount.ADULT;

public enum RequireFare {
    DEFAULT_FARE(distance -> distance > 0 && distance <= 10,
            (distance, discount) -> new BaseCalculatedFare(discount).calculateFare()),
    FIRST_ADDITIONAL_FARE(distance -> distance > 10 && distance <= 50,
            (distance, discount) -> new FirstAdditionalCalculatedFare(distance, discount).calculateFare()),
    SECOND_ADDITIONAL_FARE(distance -> distance > 50,
            (distance, discount) -> new SecondAdditionalCalculatedFare(distance, discount).calculateFare());

    private final Predicate<Integer> selector;
    private final BiFunction<Integer, Discount, Integer> calculator;

    RequireFare(Predicate<Integer> selector, BiFunction<Integer, Discount, Integer> calculator) {
        this.selector = selector;
        this.calculator = calculator;
    }

    public static int getRequireFare(Path path) {
        return getRequireFareWithDiscount(path, ADULT);
    }

    public static int getRequireFareWithDiscount(Path path, Discount discount) {
        return Arrays.stream(values())
                .filter(fareSelector -> fareSelector.selector.test(path.getDistance()))
                .findFirst()
                .map(fareSelector -> fareSelector.calculator.apply(path.getDistance(), discount))
                .map(calculatedFare -> calculatedFare + path.getFare())
                .orElse(0);
    }
}
