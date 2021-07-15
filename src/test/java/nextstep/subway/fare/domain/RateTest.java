package nextstep.subway.fare.domain;

import nextstep.subway.fare.exception.BelowZeroIntegerException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@DisplayName("비율 테스트")
class RateTest {

    @CsvSource({"0", "0.1", "0.3", "0.5", "0.7", "1"})
    @ParameterizedTest
    void constructor_성공(BigDecimal value) {
        assertDoesNotThrow(() -> new Rate(value));
    }

    @CsvSource({"-1", "-0.5"})
    @ParameterizedTest
    void constructor_예외(BigDecimal value) {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> new Rate(value));
    }

}