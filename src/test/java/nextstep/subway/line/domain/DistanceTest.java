package nextstep.subway.line.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class DistanceTest {

    @DisplayName("distance 값은 자연수만 사용할 수 있다.")
    @ValueSource(ints = {0, -1, -2, -3, -100, -500})
    @ParameterizedTest
    void positiveDistanceTest(int value) {
        assertThatExceptionOfType(ModifySectionException.class).isThrownBy(
            () -> new Distance(value)
        );
    }

    @DisplayName("plus는 두 Distance value를 더한 value를 갖는다")
    @CsvSource(value = {"1,2", "1,3", "4,5", "10,90"})
    @ParameterizedTest
    void plusTest(int a, int b) {
        Distance distance = new Distance(a);
        assertThat(distance.plus(b).getValue()).isEqualTo(a + b);
    }

    @DisplayName("plus는 두 Distance value를 뺀 value를 갖는다")
    @CsvSource(value = {"2, 1", "3, 1", "5, 4", "90,50"})
    @ParameterizedTest
    void minusTest(int a, int b) {
        Distance distance = new Distance(a);
        assertThat(distance.minus(b).getValue()).isEqualTo(a - b);
    }
}
