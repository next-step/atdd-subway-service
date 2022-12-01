package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Fare;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;

class AgeDiscountTest {
    @DisplayName("어린이와 청소년이 아닐 경우, 할인이 적용되지 않는다")
    @ParameterizedTest
    @ValueSource(ints = {19, 20, 99})
    void noDiscount(int age) {
        // when
        Fare result = AgeDiscount.calculate(age, 1_250);

        // then
        assertThat(result.value()).isEqualTo(0);
    }

    @DisplayName("어린이들은 지하철 요금의 50%를 할인 받는다")
    @ParameterizedTest
    @CsvSource(value = {"6:450", "12:450"}, delimiter = ':')
    void discountChild(int age, int fare) {
        // when
        Fare result = AgeDiscount.calculate(age, 1_250);

        // then
        assertThat(result.value()).isEqualTo(fare);
    }

    @DisplayName("청소년들은 지하철 요금의 20%를 할인 받는다")
    @ParameterizedTest
    @CsvSource(value = {"13:180", "18:180"}, delimiter = ':')
    void discountTeenager(int age, int fare) {
        // when
        Fare result = AgeDiscount.calculate(age, 1_250);

        // then
        assertThat(result.value()).isEqualTo(fare);
    }
}
