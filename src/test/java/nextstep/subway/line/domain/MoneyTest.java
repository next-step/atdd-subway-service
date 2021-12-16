package nextstep.subway.line.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class MoneyTest {

    @Test
    void plus() {
        // given
        Money money = new Money(1250);

        // when then
        assertThat(money.plus(300)).isEqualTo(new Money(1550));
    }

    @Test
    void minus() {
        // given
        Money money = new Money(1250);

        // when // then
        assertThat(money.minus(350)).isEqualTo(new Money(900));
    }

    @Test
    void multiply() {
        // given
        Money money = new Money(1250);
        Money minus = money.minus(350);

        // when then
        assertThat(minus.multiply(0.5f)).isEqualTo(new Money(450));
    }
}