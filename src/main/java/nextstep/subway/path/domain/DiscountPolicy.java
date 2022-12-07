package nextstep.subway.path.domain;

import java.util.Arrays;
import java.util.function.Function;

import nextstep.subway.amount.domain.Amount;

public enum DiscountPolicy {
    DEFAULT((age) -> age == null || age >= 19, (amount) -> amount),
    TEENAGER((age) -> age >= 13 && age < 19, (amount) -> (amount - 350) * 80 / 100),
    CHILDREN((age) -> age < 13, (amount) -> (amount - 350) * 50 / 100);

    private final Function<Integer, Boolean> policyRule;
    private final Function<Long, Long> discountRule;

    DiscountPolicy(Function<Integer, Boolean> policyRule, Function<Long, Long> discountRule) {
        this.policyRule = policyRule;
        this.discountRule = discountRule;
    }

    public static DiscountPolicy valueOf(Integer age) {
        return Arrays.stream(DiscountPolicy.values())
            .filter(discountPolicy -> discountPolicy.policyRule.apply(age))
            .findAny()
            .orElse(DEFAULT);
    }

    public Amount calculateDiscount(Amount amount) {
        return Amount.from(this.discountRule.apply(amount.value()));
    }
}
