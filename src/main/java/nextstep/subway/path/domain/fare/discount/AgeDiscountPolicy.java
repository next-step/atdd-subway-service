package nextstep.subway.path.domain.fare.discount;

import java.util.Arrays;
import java.util.function.IntUnaryOperator;
import java.util.function.Predicate;

public enum AgeDiscountPolicy implements DiscountPolicy {
    INFANT(age -> age >= 0 && age < 6, fare -> 0),
    CHILD(age -> age >= 6 && age < 13, fare -> (int) ((fare - 350) * 0.5)),
    ADOLESCENT(age -> age >= 13 && age < 19, fare -> (int) ((fare - 350) * 0.8)),
    ADULT(age -> age >= 19, fare -> fare);

    Predicate<Integer> agePredicate;
    IntUnaryOperator discountOperator;

    private static final int MIN_DISCOUNTED_FARE = 0;

    AgeDiscountPolicy(Predicate<Integer> agePredicate, IntUnaryOperator discountOperator) {
        this.agePredicate = agePredicate;
        this.discountOperator = discountOperator;
    }

    public static DiscountPolicy from(Integer age) {
        return Arrays.stream(values())
                .filter(ageDiscountPolicy -> ageDiscountPolicy.agePredicate.test(age))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("연령 정보를 확인해 주세요."));
    }

    @Override
    public int discount(int fare) {
        int discountedFare = discountOperator.applyAsInt(fare);
        if (discountedFare < MIN_DISCOUNTED_FARE) {
            throw new IllegalStateException("할인요금 계산 시 오류가 발생했습니다.");
        }
        return discountedFare;
    }
}
