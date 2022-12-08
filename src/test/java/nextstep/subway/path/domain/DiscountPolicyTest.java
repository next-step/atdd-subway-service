package nextstep.subway.path.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import nextstep.subway.amount.domain.Amount;

class DiscountPolicyTest {

    @Test
    void valueOf() {
        assertAll(
            () -> assertThat(DiscountPolicy.valueOf(20)).isEqualTo(DiscountPolicy.DEFAULT),
            () -> assertThat(DiscountPolicy.valueOf((Integer)null)).isEqualTo(DiscountPolicy.DEFAULT),
            () -> assertThat(DiscountPolicy.valueOf(15)).isEqualTo(DiscountPolicy.TEENAGER),
            () -> assertThat(DiscountPolicy.valueOf(6)).isEqualTo(DiscountPolicy.CHILDREN)
        );
    }

    @Test
    void calculateDiscount() {
        assertAll(
            () -> assertThat(DiscountPolicy.DEFAULT.calculateDiscount(Amount.from(1250))).isEqualTo(Amount.from(1250L)),
            () -> assertThat(DiscountPolicy.TEENAGER.calculateDiscount(Amount.from(1250))).isEqualTo(Amount.from(720)),
            () -> assertThat(DiscountPolicy.CHILDREN.calculateDiscount(Amount.from(1250))).isEqualTo(Amount.from(450))
        );
    }
}
