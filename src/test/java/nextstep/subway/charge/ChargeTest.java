package nextstep.subway.charge;

import static org.assertj.core.api.Assertions.assertThat;

import nextstep.subway.charge.domain.Charge;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ChargeTest {

    @DisplayName("덧셈 테스트")
    @Test
    void plus() {
        Charge 요금 = new Charge(1250);
        요금.plus(100);

        assertThat(요금.getChargeValue()).isEqualTo(1350);
    }

    @DisplayName("뺼셈 테스트")
    @Test
    void minus() {
        Charge 요금 = new Charge(1250);
        요금.minus(350);

        assertThat(요금.getChargeValue()).isEqualTo(900);
    }

    @DisplayName("곱셈 테스트")
    @Test
    void multiply() {
        Charge 요금 = new Charge(1250);
        요금.multiply(0.5);

        assertThat(요금.getChargeValue()).isEqualTo(625);
    }
}
