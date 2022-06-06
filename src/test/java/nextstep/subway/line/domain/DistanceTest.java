package nextstep.subway.line.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

class DistanceTest {
    @DisplayName("구간 길이 생성 테스트")
    @ParameterizedTest(name = "구간 길이를 {0}으로 생성했을때 실제 구간길이 값 {1} 확인 테스트")
    @CsvSource(value = {"1, 1", "10, 10"})
    void valueOf(int input, int expect) {
        assertThat(Distance.valueOf(input).distance()).isEqualTo(expect);
    }

    @DisplayName("구간 길이가 0 혹은 0보다 작은 경우 예외 테스트")
    @ParameterizedTest(name = "구간 길이가 {0}인 경우 예외 테스트")
    @ValueSource(strings = {"0", "-1", "-100"})
    void distanceZeroOrLess(int input) {
        assertThatIllegalArgumentException()
                .isThrownBy(() -> Distance.valueOf(input))
                .withMessage("구간 길이는 0 이하가 될 수 없습니다.");
    }
}

