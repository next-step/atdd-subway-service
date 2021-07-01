package nextstep.subway.path.domain.policy.discount;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class DefaultDiscountPolicyTest {

    @DisplayName("요금을 할인한다. - 기본 할인은 할인이 없는 것이다")
    @Test
    void calculate() {
        int fare = 1250;
        int expectedFare = 1250;
        DefaultDiscountPolicy policy = new DefaultDiscountPolicy();

        int calculated = policy.calculate(fare);

        assertThat(calculated).isEqualTo(expectedFare);
    }
}
