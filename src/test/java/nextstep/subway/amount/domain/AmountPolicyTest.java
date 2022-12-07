package nextstep.subway.amount.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import nextstep.subway.line.domain.Distance;

class AmountPolicyTest {

    @Test
    void valueOf() {
        assertAll(
            () -> assertThat(AmountPolicy.valueOf(Distance.from(10))).isEqualTo(AmountPolicy.DEFAULT),
            () -> assertThat(AmountPolicy.valueOf(Distance.from(50))).isEqualTo(AmountPolicy.OVER_10),
            () -> assertThat(AmountPolicy.valueOf(Distance.from(51))).isEqualTo(AmountPolicy.OVER_50)
        );
    }

    @Test
    void calculateAmount() {
        assertAll(
            () -> assertThat(AmountPolicy.DEFAULT.calculateAmount(Distance.from(10))).isEqualTo(Amount.from(1250L)),
            () -> assertThat(AmountPolicy.OVER_10.calculateAmount(Distance.from(20))).isEqualTo(Amount.from(1450L)),
            () -> assertThat(AmountPolicy.OVER_50.calculateAmount(Distance.from(58))).isEqualTo(Amount.from(2150L))
        );
    }
}
