package nextstep.subway.auth.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class DiscountPolicyTest {

    @DisplayName("성인 요금 할인 적용")
    @Test
    void AdultDiscountPolicy() {
        // given
        DiscountPolicy discountPolicy = new AdultDiscountPolicy();

        // when, then
        assertThat(discountPolicy.discount(1250)).isEqualTo(1250);
        assertThat(discountPolicy.discount(2050)).isEqualTo(2050);
        assertThat(discountPolicy.discount(3650)).isEqualTo(3650);
    }

    @DisplayName("청소년 요금 할인 적용")
    @Test
    void YouthDiscountPolicy() {
        // given
        DiscountPolicy discountPolicy = new YouthDiscountPolicy();

        // when, then
        assertThat(discountPolicy.discount(1250)).isEqualTo(720);
        assertThat(discountPolicy.discount(2050)).isEqualTo(1360);
        assertThat(discountPolicy.discount(3650)).isEqualTo(2640);
    }

    @DisplayName("어린이 요금 할인 적용")
    @Test
    void ChildDiscountPolicy() {
        // given
        DiscountPolicy discountPolicy = new ChildDiscountPolicy();

        // when, then
        assertThat(discountPolicy.discount(1250)).isEqualTo(450);
        assertThat(discountPolicy.discount(2050)).isEqualTo(850);
        assertThat(discountPolicy.discount(3650)).isEqualTo(1650);
    }
}
