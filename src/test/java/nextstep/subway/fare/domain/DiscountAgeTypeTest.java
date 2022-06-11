package nextstep.subway.fare.domain;

import static org.assertj.core.api.Assertions.assertThat;

import nextstep.subway.policy.DiscountPolicy;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class DiscountAgeTypeTest {

    @DisplayName("0~6살 미만의 나이는 TODDLER 타입이다.")
    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2, 3, 4, 5})
    void discountAgeRuleType01(int age) {
        // given & when
        DiscountAgeType type = DiscountAgeType.findDiscountAgeRuleType(age);

        // then
        assertThat(type).isEqualTo(DiscountAgeType.TODDLER);
    }

    @DisplayName("6 ~ 13살 미만의 나이는 CHILDREN 타입이다.")
    @ParameterizedTest
    @ValueSource(ints = {6, 7, 8, 9, 10, 11, 12})
    void discountAgeRuleType02(int age) {
        // given & when
        DiscountAgeType type = DiscountAgeType.findDiscountAgeRuleType(age);

        // then
        assertThat(type).isEqualTo(DiscountAgeType.CHILDREN);
    }

    @DisplayName("13 ~ 19살 미만의 나이는 TEENAGER 타입이다.")
    @ParameterizedTest
    @ValueSource(ints = {13, 14, 15, 16, 17, 18})
    void discountAgeRuleType03(int age) {
        // given & when
        DiscountAgeType type = DiscountAgeType.findDiscountAgeRuleType(age);

        // then
        assertThat(type).isEqualTo(DiscountAgeType.TEENAGER);
    }

    @DisplayName("19살 이상의 나이는 ADULT 타입이다.")
    @ParameterizedTest
    @ValueSource(ints = {19, 29, 59, 89, 109})
    void discountAgeRuleType04(int age) {
        // given & when
        DiscountAgeType type = DiscountAgeType.findDiscountAgeRuleType(age);

        // then
        assertThat(type).isEqualTo(DiscountAgeType.ADULT);
    }

    @DisplayName("CHILDREN 은 350원을 공제한 금액의 50%를 할인받는다.")
    @ParameterizedTest
    @ValueSource(ints = {6, 7, 8, 9, 10, 11, 12})
    void discount01(int age) {
        // given
        int fare = 1250;
        DiscountAgeType discountAgeRuleType = DiscountAgeType.findDiscountAgeRuleType(age);
        DiscountPolicy discountPolicy = discountAgeRuleType.getDiscountPolicy();

        // when
        int discountedFare = discountPolicy.discountFare(fare);

        // then
        //  1250 - 350 = 900 -> 900 * 0.5 = 450
        assertThat(discountedFare).isEqualTo(450);
    }

    @DisplayName("TEENAGER 은 350원을 공제한 금액의 20%를 할인받는다.")
    @ParameterizedTest
    @ValueSource(ints = {13, 14, 15, 16, 17, 18})
    void discount02(int age) {
        // given
        int fare = 1250;
        DiscountAgeType discountAgeRuleType = DiscountAgeType.findDiscountAgeRuleType(age);
        DiscountPolicy discountPolicy = discountAgeRuleType.getDiscountPolicy();

        // when
        int discountedFare = discountPolicy.discountFare(fare);

        // then
        //  1250 - 350 = 900 -> 900 * 0.8 = 450
        assertThat(discountedFare).isEqualTo(720);
    }

    @DisplayName("TODDLER 은 공짜다!")
    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2, 3, 4, 5})
    void discount03(int age) {
        // given
        int fare = 1250;
        DiscountAgeType discountAgeRuleType = DiscountAgeType.findDiscountAgeRuleType(age);
        DiscountPolicy discountPolicy = discountAgeRuleType.getDiscountPolicy();

        // when
        int discountedFare = discountPolicy.discountFare(fare);

        // then
        assertThat(discountedFare).isZero();
    }


    @DisplayName("ADULT는 할인이 없다.")
    @ParameterizedTest
    @ValueSource(ints = {19, 29, 59, 89, 109})
    void discount04(int age) {
        // given
        int fare = 1250;
        DiscountAgeType discountAgeRuleType = DiscountAgeType.findDiscountAgeRuleType(age);
        DiscountPolicy discountPolicy = discountAgeRuleType.getDiscountPolicy();

        // when
        int discountedFare = discountPolicy.discountFare(fare);

        // then
        assertThat(discountedFare).isEqualTo(1250);
    }

}