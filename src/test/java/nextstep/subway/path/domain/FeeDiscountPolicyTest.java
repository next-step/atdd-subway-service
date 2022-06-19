package nextstep.subway.path.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class FeeDiscountPolicyTest {

    @Test
    @DisplayName("요금 할인 정책 객체가 같은지 검증")
    void verifySameFeeDiscountPolicy() {
        FeeDiscountPolicy feeDiscountPolicy = new FeeDiscountPolicy(1500, 10);
        assertThat(feeDiscountPolicy).isEqualTo(new FeeDiscountPolicy(1500, 10));
    }

    @ParameterizedTest(name = "청소년({0}) 요금할인이 잘 적용되는지 검증")
    @ValueSource(ints = {13, 18})
    void discountTeenFee(int age) {
        assertThat(new FeeDiscountPolicy(1500, age).getFee()).isEqualTo(920);
    }

    @ParameterizedTest(name = "어린이({0}) 요금할인이 잘 적용되는지 검증")
    @ValueSource(ints = {6, 12})
    void discountChildFee(int age) {
        assertThat(new FeeDiscountPolicy(1500, age).getFee()).isEqualTo(575);
    }

    @ParameterizedTest(name = "청소년, 어린이 외는 요금할인이 적용되지 않는지 검증")
    @ValueSource(ints = {5, 19})
    void notDiscountOtherFee(int age) {
        assertThat(new FeeDiscountPolicy(1500, age).getFee()).isEqualTo(1500);
    }
}
