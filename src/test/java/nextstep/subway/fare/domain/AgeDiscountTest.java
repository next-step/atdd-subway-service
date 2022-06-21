package nextstep.subway.fare.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class AgeDiscountTest {

    @Test
    @DisplayName("성인의 경우 할인이 적용되지 않는다")
    void discountFareForGeneral() {
        // given
        AgeDiscount discountPolicy = new AgeDiscount(20);
        int fare = 2000;

        // when
        int actual = discountPolicy.discountFare(fare);

        // then
        assertThat(actual).isEqualTo(fare);
    }

    @Test
    @DisplayName("청소년의 경우 성인 요금의 350원을 제한 요금의 20%를 할인한다")
    void discountFareForTeenager() {
        // given
        AgeDiscount discountPolicy = new AgeDiscount(17);
        int fare = 2000;

        // when
        int actual = discountPolicy.discountFare(fare);

        // then
        assertThat(actual).isEqualTo((int)((fare - 350) * 0.8));
    }

    @Test
    @DisplayName("어린이의 경우 성인 요금의 350원을 제한 요금의 50%를 할인한다")
    void discountFareForChild() {
        // given
        AgeDiscount discountPolicy = new AgeDiscount(10);
        int fare = 2000;

        // when
        int actual = discountPolicy.discountFare(fare);

        // then
        assertThat(actual).isEqualTo((int)((fare - 350) * 0.5));
    }

    @Test
    @DisplayName("우대의 경우 무임을 적용한다")
    void discountFareForPreferential() {
        // given
        AgeDiscount discountPolicy = new AgeDiscount(70);
        int fare = 2000;

        // when
        int actual = discountPolicy.discountFare(fare);

        // then
        assertThat(actual).isZero();
    }

}
