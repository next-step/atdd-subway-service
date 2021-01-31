package nextstep.subway.member;

import nextstep.subway.member.domain.*;
import nextstep.subway.member.dto.Money;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class DiscountStrategyTest {

    @DisplayName("어린이 운임 요금 테스트")
    @Test
    public void discountTest1() {
        DiscountStrategy discountStrategy = new ChildDiscountStrategy();
        assertThat(discountStrategy.discount(Money.of(1000))).isEqualTo(Money.of(675));
    }

    @DisplayName("청소년 운임 요금 테스트")
    @Test
    public void discountTest2() {
        DiscountStrategy discountStrategy = new TeenagerDiscountStrategy();
        assertThat(discountStrategy.discount(Money.of(1000))).isEqualTo(Money.of(870));
    }

    @DisplayName("어른이 운임요금 테스트")
    @Test
    public void discountTest3() {
        DiscountStrategy discountStrategy = new NoDiscountStrategy();
        assertThat(discountStrategy.discount(Money.of(1000))).isEqualTo(Money.of(1000));
    }
}
