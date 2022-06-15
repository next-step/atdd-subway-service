package nextstep.subway.fare.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class FareTest {
    @DisplayName("요금 초기화 테스트")
    @Test
    void fare() {
        assertThat(Fare.valueOf(10000).fare()).isEqualTo(10000);
    }

    @DisplayName("음수로 요금 초기화하면 IllegalArgumentException 예외")
    @ParameterizedTest(name = "음수 {0}로 요금을 초기화하면 IllegalArgumentException 예외")
    @ValueSource(ints = {-1, -100, -10000})
    void fareNegativeNumber(int input) {
        assertThatIllegalArgumentException()
                .isThrownBy(() -> Fare.valueOf(input))
                .withMessage("음수는 유효하지 않습니다.");
    }
}
