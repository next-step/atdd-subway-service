package nextstep.subway.auth.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("멤버 요금 테스트")
class MemberMoneyTest {

    @DisplayName("멤버 요금 뺄셈")
    @Test
    void minus_memberMoney_success() {
        // given:
        final double source = 300;
        final double target = 100;
        final MemberMoney sourceMoney = MemberMoney.from(Money.from(source));
        final MemberMoney targetMoney = MemberMoney.from(Money.from(target));
        // when,then:
        assertThat(sourceMoney.minus(targetMoney)).isEqualTo(MemberMoney.from(Money.from(source - target)));
    }

    @DisplayName("멤버 요금 소숫점 나누기")
    @Test
    void divideByDecimalPoint_memberMoney_success() {
        // given:
        final double source = 100;
        final double rate = 0.5;
        final double expected = 50;
        final MemberMoney sourceMoney = MemberMoney.from(Money.from(source));
        // when,then:
        assertThat(sourceMoney.divideByDecimalPoint(rate)).isEqualTo(MemberMoney.from(Money.from(expected)));
    }
}
