package nextstep.subway.line.domain;

import nextstep.subway.line.exception.money.IllegalMoneyException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * packageName : nextstep.subway.line.domain
 * fileName : MoneyTest
 * author : haedoang
 * date : 2021/12/12
 * description :
 */
public class MoneyTest {

    @Test
    @DisplayName("돈 객체 생성하기")
    public void create() throws Exception {
        // given
        Money money = Money.of(1_000);

        // when
        Money loseMoney = money.minus(Money.of(500));

        // then
        assertThat(loseMoney).isEqualTo(Money.of(500));

        // when
        Money earnMoney = money.plus(Money.of(500));

        // then
        assertThat(earnMoney).isEqualTo(Money.of(1_500));
    }

    @Test
    @DisplayName("돈 객체 유효성 체크하기")
    public void validate() throws Exception {
        //given
        int illegalMoney = -500;
        Money money = Money.of(500);

        assertThatThrownBy(() -> Money.of(illegalMoney)).isInstanceOf(IllegalMoneyException.class)
                .hasMessage(String.format(IllegalMoneyException.message, Money.MIN_VALUE));

        assertThatThrownBy(() -> money.minus(Money.of(600))).isInstanceOf(IllegalMoneyException.class)
                .hasMessage(String.format(IllegalMoneyException.message, Money.MIN_VALUE));
    }
}
