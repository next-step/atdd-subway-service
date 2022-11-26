package nextstep.subway.line.domain;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

public class ColorTest {

    @DisplayName("색상이 Null 또는 빈 값이면 에러가 발생한다.")
    @ParameterizedTest
    @NullAndEmptySource
    void validateNullOrEmptyException(String color) {
        assertThatThrownBy(() -> Color.from(color))
            .isInstanceOf(IllegalArgumentException.class);
    }
}
