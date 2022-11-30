package nextstep.subway.line.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import nextstep.subway.exception.DistanceException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class DistanceTest {

    @DisplayName("거리가 0이하면 에러가 발생한다.")
    @ParameterizedTest
    @ValueSource(ints = { 0, -1 })
    void validationZeroLessException(int distance) {
        assertThatThrownBy(() -> Distance.from(distance))
            .isInstanceOf(DistanceException.class);
    }

    @DisplayName("거리에서 거리를 빼면 거리가 계산된다.")
    @Test
    void subtract() {
        Distance distance1 = Distance.from(10);
        Distance distance2 = Distance.from(5);

        Distance result = distance1.substract(distance2);

        assertThat(result.value()).isEqualTo(5);
    }
}
