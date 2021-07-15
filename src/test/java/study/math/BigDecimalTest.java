package study.math;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.math.BigDecimal;

import static java.math.BigDecimal.valueOf;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("BigDecimal 학습 테스트")
class BigDecimalTest {

    @CsvSource(value = {
            "100:50:1",
            "100:100:0",
            "100:150:-1"
    }, delimiterString = ":")
    @ParameterizedTest
    void compareTo_성공(int leftValue, int rightValue, int expectedResult) {
        // given
        BigDecimal left = valueOf(leftValue);
        BigDecimal right = valueOf(rightValue);

        assertThat(left.compareTo(right)).isEqualTo(expectedResult);
    }
}
