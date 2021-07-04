package nextstep.subway.line.domain;

import nextstep.subway.line.exception.BelowZeroDistanceException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@DisplayName("Distnace 도메인 테스트")
class DistanceTest {

    @ValueSource(ints = {1, 2, 3, 4, 5, 6, 7})
    @ParameterizedTest
    void new_성공(int param) {
        assertDoesNotThrow(() -> new Distance(param));
    }

    @ValueSource(ints = {0, -5, -10, -30, -40})
    @ParameterizedTest
    void new_예외_0이하_불가(int param) {
        assertThatExceptionOfType(BelowZeroDistanceException.class)
                .isThrownBy(() -> new Distance(param));
    }

    @CsvSource(value = {"3:1:4", "4:2:6", "5:3:8"}, delimiterString = ":")
    @ParameterizedTest
    void add_성공(int value1, int value2, int expectedResult) {
        // given
        Distance distance1 = new Distance(value1);
        Distance distance2 = new Distance(value2);
        Distance expectedDistance = new Distance(expectedResult);

        assertDoesNotThrow(() -> distance1.add(distance2));
        assertThat(distance1.add(distance2)).isEqualTo(expectedDistance);
    }

    @CsvSource(value = {"3:1:2", "6:2:4", "5:3:2"}, delimiterString = ":")
    @ParameterizedTest
    void minus_성공(int value1, int value2, int expectedResult) {
        // given
        Distance distance1 = new Distance(value1);
        Distance distance2 = new Distance(value2);
        Distance expectedDistance = new Distance(expectedResult);

        assertDoesNotThrow(() -> distance1.minus(distance2));
        assertThat(distance1.minus(distance2)).isEqualTo(expectedDistance);
    }

    @CsvSource(value = {"3:5", "4:7", "5:10"}, delimiterString = ":")
    @ParameterizedTest
    void minus_예외(int value1, int value2) {
        // given
        Distance distance1 = new Distance(value1);
        Distance distance2 = new Distance(value2);

        assertThatExceptionOfType(BelowZeroDistanceException.class)
                .isThrownBy(() -> distance1.minus(distance2));
    }
}