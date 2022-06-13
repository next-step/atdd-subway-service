package nextstep.subway.line.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class DistanceTest {
    @DisplayName("거리의 합을 반환한다.")
    @ParameterizedTest
    @CsvSource(value = {"1:1:2", "1:2:3"}, delimiter = ':')
    void addTest(int distance, int value, int expected) {
        Distance actual = Distance.from(distance);
        Distance addValue = Distance.from(value);

        actual.add(addValue);

        assertThat(actual.getDistance()).isEqualTo(expected);
    }

    @DisplayName("거리의 차를 반환한다.")
    @ParameterizedTest
    @CsvSource(value = {"2:1:1", "3:2:1"}, delimiter = ':')
    void subtractTest(int distance, int value, int expected) {
        Distance actual = Distance.from(distance);
        Distance subtractValue = Distance.from(value);

        actual.subtract(subtractValue);

        assertThat(actual.getDistance()).isEqualTo(expected);
    }

    @DisplayName("거리의 차가 음수이면 예외를 반환한다.")
    @ParameterizedTest
    @CsvSource(value = {"1:2:-1", "10:11:-1"}, delimiter = ':')
    void subtractExceptionTest(int distance, int value, int expected) {
        Distance actual = Distance.from(distance);
        Distance subtractValue = Distance.from(value);

        assertThatThrownBy(() -> {
            actual.subtract(subtractValue);
        }).isInstanceOf(IllegalArgumentException.class);
    }
}
