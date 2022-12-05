package nextstep.subway.line.domain;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class ExtraFareTest {

    @DisplayName("요금이 0보다 작으면 에러가 발생한다.")
    @ParameterizedTest
    @ValueSource(ints = { -1, -10 })
    void validationZeroLessException(int extraFare) {
        assertThatThrownBy(() -> ExtraFare.from(extraFare))
            .isInstanceOf(IllegalArgumentException.class);
    }
}
