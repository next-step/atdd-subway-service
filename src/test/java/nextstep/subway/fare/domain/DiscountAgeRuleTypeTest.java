package nextstep.subway.fare.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class DiscountAgeRuleTypeTest {

    @DisplayName("0~6살 미만의 나이는 TODDLER 타입이다.")
    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2, 3, 4, 5})
    void discountAgeRuleType01(int age) {
        // given & when
        DiscountAgeRuleType type = DiscountAgeRuleType.findDiscountAgeRuleType(age);

        // then
        assertThat(type).isEqualTo(DiscountAgeRuleType.TODDLER);
    }

    @DisplayName("6 ~ 13살 미만의 나이는 CHILDREN 타입이다.")
    @ParameterizedTest
    @ValueSource(ints = {6, 7, 8, 9, 10, 11, 12})
    void discountAgeRuleType02(int age) {
        // given & when
        DiscountAgeRuleType type = DiscountAgeRuleType.findDiscountAgeRuleType(age);

        // then
        assertThat(type).isEqualTo(DiscountAgeRuleType.CHILDREN);
    }

    @DisplayName("13 ~ 19살 미만의 나이는 TEENAGER 타입이다.")
    @ParameterizedTest
    @ValueSource(ints = {13, 14, 15, 16, 17, 18})
    void discountAgeRuleType03(int age) {
        // given & when
        DiscountAgeRuleType type = DiscountAgeRuleType.findDiscountAgeRuleType(age);

        // then
        assertThat(type).isEqualTo(DiscountAgeRuleType.TEENAGER);
    }

    @DisplayName("19살 이상의 나이는 ADULT 타입이다.")
    @ParameterizedTest
    @ValueSource(ints = {19, 29, 59, 89, 109})
    void discountAgeRuleType04(int age) {
        // given & when
        DiscountAgeRuleType type = DiscountAgeRuleType.findDiscountAgeRuleType(age);

        // then
        assertThat(type).isEqualTo(DiscountAgeRuleType.ADULT);
    }

}