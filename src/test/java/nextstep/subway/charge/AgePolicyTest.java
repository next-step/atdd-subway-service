package nextstep.subway.charge;

import static org.assertj.core.api.Assertions.assertThat;

import nextstep.subway.charge.domain.AgePolicy;
import nextstep.subway.charge.domain.Charge;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class AgePolicyTest {

    @DisplayName("일반 연령대일 경우")
    @Test
    void applyNormalPolicy() {
        //given
        Charge charge = new Charge(1250);
        AgePolicy normalAgePolicy = new AgePolicy(30);

        //when
        normalAgePolicy.applyPolicy(charge);

        //then
        assertThat(charge.getChargeValue()).isEqualTo(1250);
    }

    @DisplayName("어린이 연령대일 경우")
    @Test
    void applyKidPolicy() {
        //given
        Charge charge = new Charge(1250);
        AgePolicy normalAgePolicy = new AgePolicy(10);

        //when
        normalAgePolicy.applyPolicy(charge);

        //then
        assertThat(charge.getChargeValue()).isEqualTo(450);
    }

    @DisplayName("청소년 연령대일 경우")
    @Test
    void applyYouthPolicy() {
        //given
        Charge charge = new Charge(1250);
        AgePolicy normalAgePolicy = new AgePolicy(15);

        //when
        normalAgePolicy.applyPolicy(charge);

        //then
        assertThat(charge.getChargeValue()).isEqualTo(720);
    }
}
