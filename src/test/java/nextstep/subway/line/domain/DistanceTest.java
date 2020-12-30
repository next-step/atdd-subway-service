package nextstep.subway.line.domain;

import nextstep.subway.line.domain.exceptions.InvalidDistanceValueException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class DistanceTest {
    private Distance one;
    private Distance two;
    private Distance three;

    @BeforeEach
    void setup() {
        one = new Distance(1);
        two = new Distance(2);
        three = new Distance(3);
    }

    @DisplayName("0이하의 값으로 객체를 생성할 수 없다.")
    @ParameterizedTest
    @ValueSource(ints = { 0, -1, -2 })
    void createFailByNegativeValueTest(int invalidValue) {
        assertThatThrownBy(() -> new Distance(invalidValue)).isInstanceOf(InvalidDistanceValueException.class);
    }

    @DisplayName("객체간 더하기가 가능하다.")
    @Test
    void plusTest() {
        assertThat(one.plus(two)).isEqualTo(three);
    }

    @DisplayName("객체간 빼기가 가능하다.")
    @Test
    void minusTest() {
        assertThat(two.minus(one)).isEqualTo(one);
    }

    @DisplayName("결과가 음수인 뺄셈은 불가능하다.")
    @Test
    void minusFailTest() {
        assertThatThrownBy(() -> one.minus(two)).isInstanceOf(InvalidDistanceValueException.class);
    }
}