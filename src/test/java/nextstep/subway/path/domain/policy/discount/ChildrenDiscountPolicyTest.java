package nextstep.subway.path.domain.policy.discount;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ChildrenDiscountPolicyTest {

    @DisplayName("연령별 할인 요금을 적용한다. - 어린이(6세이상 ~ 13세 미만) 결과에서 350원 공제한 값의 50% 할인")
    @Test
    void calculate() {
        int fare = 1250;
        int expectedFare = 800;
        ChildrenDiscountPolicy policy = new ChildrenDiscountPolicy();

        int calculated = policy.calculate(fare);

        assertThat(calculated).isEqualTo(expectedFare);
    }
}
