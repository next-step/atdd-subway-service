package nextstep.subway.policy;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class DiscountPolicyTest {

    @DisplayName("FreeDiscountPolicy 는 요금이 0원으로 할인된다.")
    @ParameterizedTest
    @CsvSource(value = {"1250,0", "5000,0"})
    void policy01(int fare, int discountedFare) {
        // given
        DiscountPolicy discountPolicy = new FreeDiscountPolicy();

        // when & then
        assertThat(discountPolicy.discountFare(fare)).isEqualTo(discountedFare);
    }

    @DisplayName("NoneDiscountPolicy 는 요금 할인이 없다. 즉, fare 그대로이다.")
    @ParameterizedTest
    @CsvSource(value = {"1250,1250", "5000,5000"})
    void policy02(int fare, int discountedFare) {
        // given
        DiscountPolicy discountPolicy = new NonDiscountPolicy();

        // when & then
        assertThat(discountPolicy.discountFare(fare)).isEqualTo(discountedFare);
    }

    @DisplayName("ChildrenAgeDiscountPolicy 는 기본 350을 공제한 금액의 50% 할인이 적용된다.")
    @ParameterizedTest
    @CsvSource(value = {"1250,450", "5000,2325"})
    void policy03(int fare, int discountedFare) {
        // given
        DiscountPolicy discountPolicy = new ChildrenAgeDiscountPolicy();

        // when & then
        assertThat(discountPolicy.discountFare(fare)).isEqualTo(discountedFare);
    }

    @DisplayName("TeenagerAgeDiscountPolicy 는 기본 350을 공제한 금액의 20% 할인이 적용된다.")
    @ParameterizedTest
    @CsvSource(value = {"1250,720", "5000,3720"})
    void policy04(int fare, int discountedFare) {
        // given
        DiscountPolicy discountPolicy = new TeenagerAgeDiscountPolicy();

        // when & then
        assertThat(discountPolicy.discountFare(fare)).isEqualTo(discountedFare);
    }
}