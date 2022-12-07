package nextstep.subway.amount.domain;

import java.util.Arrays;
import java.util.function.Function;

import nextstep.subway.line.domain.Distance;

public enum AmountPolicy {
    DEFAULT((distance) -> distance <= 10, (distance) -> 1250L),
    OVER_10((distance) -> distance > 10 && distance <= 50, (distance) -> 1250L + (distance - 10) / 5 * 100L),
    OVER_50((distance) -> distance > 50, (distance) -> 2050L + (distance - 50) / 8 * 100L);

    private final Function<Integer, Boolean> policyRule;
    private final Function<Integer, Long> amountRule;

    AmountPolicy(Function<Integer, Boolean> policyRule, Function<Integer, Long> amountRule) {
        this.policyRule = policyRule;
        this.amountRule = amountRule;
    }

    public static AmountPolicy valueOf(Distance distance) {
        return Arrays.stream(AmountPolicy.values())
            .filter(amountPolicy -> amountPolicy.policyRule.apply(distance.value()))
            .findAny()
            .orElse(DEFAULT);
    }

    public Amount calculateAmount(Distance distance) {
        return Amount.from(this.amountRule.apply(distance.value()));
    }
}
