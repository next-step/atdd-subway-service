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
        Fare fare = Fare.valueOf(input);
        fare.add(Fare.valueOf(other));
        assertThat(fare).isEqualTo(Fare.valueOf(expect));
    }

    @DisplayName("요금 끼리는 뺄 수 있다.")
    @ParameterizedTest(name = "요금 {0}은 {1}을 빼 {2}가 된다.")
    @CsvSource(value = {"10, 5, 5", "20, 1, 19"})
    void minus(int input, int other, int expect) {
        Fare fare = Fare.valueOf(input);
        fare.minus(Fare.valueOf(other));
        assertThat(fare).isEqualTo(Fare.valueOf(expect));
    }

    @DisplayName("요금은 음수가 나올 경우 예외가 발생한다.")
    @ParameterizedTest(name = "요금 {0}은 {1}을 빼 음수가되면 예외가 발생한다.")
    @CsvSource(value = {"10, 15", "20, 25"})
    void minusNegativeCase(int input, int other) {
        Fare fare = Fare.valueOf(input);
        assertThatIllegalArgumentException()
                .isThrownBy(() -> fare.minus(Fare.valueOf(other)))
                .withMessage("음수는 유효하지 않습니다.");
    }

    @DisplayName("요금은 곱할 수 있다.")
    @ParameterizedTest(name = "요금 {0}은 {1}와 곱해 요금 {2}이 된다.")
    @CsvSource(value = {"1000:20:20000", "100:0:0", "10000:10:100000"}, delimiter = ':')
    void multiply(int input, int other, int expect) {
        Fare fare = Fare.valueOf(input);
        fare.multiply(other);
        assertThat(fare).isEqualTo(Fare.valueOf(expect));
    }

    @DisplayName("요금은 나눌 수 있다.")
    @ParameterizedTest(name = "요금 {0}은 {1}로 나눠 {2}이 된다.")
    @CsvSource(value = {"1000:2:500", "100:3:33"}, delimiter = ':')
    void divideBy(int input, int other, int expect) {
        Fare fare = Fare.valueOf(input);
        fare.divideBy(other);
        assertThat(fare).isEqualTo(Fare.valueOf(expect));
    }
}
