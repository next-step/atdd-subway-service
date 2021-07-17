package nextstep.subway.member.domain;

import nextstep.subway.fare.exception.BelowZeroIntegerException;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class AgeTest {

    @ValueSource(ints = {10, 30, 50, 80})
    @ParameterizedTest
    void constructor_성공(int value) {
        assertDoesNotThrow(() -> new Age(value));
    }

    @ValueSource(ints = {0, -10, -50, -80})
    @ParameterizedTest
    void constructor_예외(int value) {
        assertThatExceptionOfType(BelowZeroIntegerException.class)
                .isThrownBy(() -> new Age(value));
    }
}