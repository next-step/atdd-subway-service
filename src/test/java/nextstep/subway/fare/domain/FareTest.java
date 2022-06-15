package nextstep.subway.fare.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
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

    @DisplayName("요금 끼리는 더할 수 있다.")
    @ParameterizedTest(name = "요금 {0}은 {1}와 더해 {2}이 된다.")
    @CsvSource(value = {"1000:2000:3000", "100:0:100", "10000:10:10010"}, delimiter = ':')
    void add(int input, int other, int expect) {
        assertThat(Fare.valueOf(input).add(Fare.valueOf(other))).isEqualTo(Fare.valueOf(expect));
    }
}
