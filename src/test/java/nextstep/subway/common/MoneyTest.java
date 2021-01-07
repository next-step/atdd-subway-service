package nextstep.subway.common;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class MoneyTest {

    @DisplayName("금액은 0이상의 양수이다.")
    @ParameterizedTest
    @ValueSource(ints = {0, 100})
    void create(int value) {
        // when
        Money money = Money.valueOf(value);

        // then
        assertThat(money).isNotNull();
    }

    @DisplayName("금액은 음수 일 수 없다.")
    @Test
    void createNotNegative() {
        // when / then
        assertThrows(IllegalArgumentException.class, () -> Money.valueOf(-1));
    }

    @DisplayName("금액이 같으면 동등성을 보장한다.")
    @ParameterizedTest
    @ValueSource(ints = {0, 100})
    void equals(int value) {
        // given
        Money source = Money.valueOf(value);
        Money target = Money.valueOf(value);

        // when
        boolean equals = source.equals(target);

        // then
        assertThat(equals).isTrue();
    }

    @DisplayName("금액을 더할 수 있다.")
    @Test
    void add() {
        // given
        Money ten = Money.valueOf(10);
        Money five = Money.valueOf(5);

        // when
        Money actual = ten.add(five);

        // then
        assertThat(actual).isEqualTo(Money.valueOf(15));
    }

    @DisplayName("금액을 뺄 수 있다.")
    @Test
    void subtract() {
        // given
        Money ten = Money.valueOf(10);
        Money five = Money.valueOf(5);

        // when
        Money actual = ten.subtract(five);

        // then
        assertThat(actual).isEqualTo(five);
    }

    @DisplayName("금액을 곱할 수 있다.")
    @Test
    void multiply() {
        // given
        Money five = Money.valueOf(5);

        // when
        Money actual = five.multiply(3);

        // then
        assertThat(actual).isEqualTo(Money.valueOf(15));
    }

}
