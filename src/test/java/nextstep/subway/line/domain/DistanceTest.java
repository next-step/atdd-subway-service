package nextstep.subway.line.domain;

import nextstep.subway.line.domain.exceptions.InvalidDistanceValueException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class DistanceTest {
    @DisplayName("음수로 객체를 생성할 수 없다.")
    @ParameterizedTest
    @ValueSource(ints = { -1, -2 })
    void createFailByNegativeValueTest(int invalidValue) {
        assertThatThrownBy(() -> new Distance(invalidValue)).isInstanceOf(InvalidDistanceValueException.class);
    }
}