package nextstep.subway.path.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

class DiscountAgeTest {
    @DisplayName("어린이들은 지하철 요금의 50%를 할인 받는다")
    @ParameterizedTest
    @CsvSource(value = {"6:450", "12:450"}, delimiter = ':')
    void discountChild(int age, int fare) {
        // when
        int result = DiscountAge.calculate(age, 1_250);

        // then
        assertThat(result).isEqualTo(fare);
    }

    @DisplayName("청소년들은 지하철 요금의 20%를 할인 받는다")
    @ParameterizedTest
    @CsvSource(value = {"13:180", "18:180"}, delimiter = ':')
    void discountTeenager(int age, int fare) {
        // when
        int result = DiscountAge.calculate(age, 1_250);

        // then
        assertThat(result).isEqualTo(fare);
    }
}
