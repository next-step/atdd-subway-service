package nextstep.subway.auth.domain;

import static org.assertj.core.api.Assertions.assertThat;

import nextstep.subway.auth.domain.discount.DiscountPolicy;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class AgeTest {

    @DisplayName("나이별로 적용되는 할인율이 달라진다")
    @ParameterizedTest
    @CsvSource(value = {"13:2150:1790", "6:2150:1250", "20:2150:2150"}, delimiter = ':')
    void createDiscountPolicy(int ageParam, int cost, int expectedCost) {
        Age age = new Age(ageParam);

        DiscountPolicy discountPolicy = age.createDiscountPolicy();

        assertThat(discountPolicy.discount(cost)).isEqualTo(expectedCost);
    }
}
