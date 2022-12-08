package nextstep.subway.auth.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

@DisplayName("요금 테스트")
class MoneyTest {

    @DisplayName("생성 성공")
    @Test
    void create_money_success() {
        final double source = 100;
        final Money money = new Money(source);
        assertThat(money.getAmount()).isEqualTo(source);
    }

    @DisplayName("요금 합")
    @Test
    void plus_money_success() {
        final double source = 100;
        final double target = 300;
        final Money sourceMoney = new Money(source);
        final Money targetMoney = new Money(target);
        assertThat(sourceMoney.plus(targetMoney)).isEqualTo(new Money(source + target));
    }

    @DisplayName("요금 뺄셈")
    @Test
    void minus_money_success() {
        final double source = 300;
        final double target = 100;
        final Money sourceMoney = new Money(source);
        final Money targetMoney = new Money(target);
        assertThat(sourceMoney.minus(targetMoney)).isEqualTo(new Money(source - target));
    }

    @DisplayName("요금 뺄셈 - 차감하고자 하는 요금이 더 큰경우")
    @Test
    void minus_money_IllegalArgumentException() {
        final double source = 100;
        final double target = 300;
        final Money sourceMoney = new Money(source);
        final Money targetMoney = new Money(target);
        assertThatIllegalArgumentException().isThrownBy(() -> sourceMoney.minus(targetMoney));
    }

    @DisplayName("소숫점 나누기")
    @Test
    void divideByDecimalPoint_money_success() {
        final double source = 100;
        final double rate = 0.5;
        final double expected = 50;
        final Money sourceMoney = new Money(source);
        assertThat(sourceMoney.divideByDecimalPoint(rate)).isEqualTo(new Money(expected));
    }
}
