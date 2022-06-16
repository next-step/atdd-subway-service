package nextstep.subway.line.domain;

import nextstep.subway.exception.InvalidSurchargeException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SurchargeTest {

    @DisplayName("Surcharge 예외 생성")
    @ParameterizedTest
    @ValueSource(ints = { -10, -5, -1 })
    void distance(int value) {
        assertThatThrownBy(() -> {
            new Surcharge(value);
        }).isInstanceOf(InvalidSurchargeException.class);
    }
}
