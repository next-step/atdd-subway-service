package nextstep.subway.path.domain.policy.discount;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TeenagerDiscountPolicyTest {

    @DisplayName("연령별 할인 요금을 적용한다. - 청소년(13세이상 ~ 19세 미만) 결과에서 350원 공제한 값의 20% 할인")
    @Test
    void calculate() {
        int fare = 1250;
        int expectedFare = 1070;
        TeenagerDiscountPolicy policy = new TeenagerDiscountPolicy();

        int calculated = policy.calculate(fare);

        assertThat(calculated).isEqualTo(expectedFare);
    }
}
