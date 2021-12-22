package nextstep.subway.path.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class AgeDiscountCalculatorTest {

    @DisplayName("나이별 할인 확인 - 성인")
    @ParameterizedTest
    @CsvSource(value = { "1050:19:1050", "2550:30:2550" }, delimiter = ':')
    void 성인_나이_할인_확인(int fare, int age, int expected) {
        // when
        int 요금 = AgeDiscountCalculator.discount(fare, age);

        // then
        assertThat(요금).isEqualTo(expected);
    }
    
    @DisplayName("나이별 할인 확인 - 어린이")
    @ParameterizedTest
    @CsvSource(value = { "1050:6:350", "2550:12:1100" }, delimiter = ':')
    void 어린이_나이_할인_확인(int fare, int age, int expected) {
        // when
        int 요금 = AgeDiscountCalculator.discount(fare, age);

        // then
        assertThat(요금).isEqualTo(expected);
    }
    
    @DisplayName("나이별 할인 확인 - 청소년")
    @ParameterizedTest
    @CsvSource(value = { "1050:13:560", "2550:18:1760" }, delimiter = ':')
    void 청소년_나이_할인_확인(int fare, int age, int expected) {
        // when
        int 요금 = AgeDiscountCalculator.discount(fare, age);

        // then
        assertThat(요금).isEqualTo(expected);
    }
}
