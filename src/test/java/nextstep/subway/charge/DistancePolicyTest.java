package nextstep.subway.charge;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import nextstep.subway.charge.domain.AgePolicy;
import nextstep.subway.charge.domain.Charge;
import nextstep.subway.charge.domain.DistancePolicy;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class DistancePolicyTest {

    @DisplayName("전체거리가 0이하일 경우")
    @Test
    void constructWithNegativeDistance() {
        assertThatThrownBy(() -> new DistancePolicy(-3)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("전체 거리가 10km 이하인 경우")
    @Test
    void applyNormalPolicy() {
        //given
        Charge charge = new Charge(1250);
        DistancePolicy normalDistancePolicy = new DistancePolicy(9);

        //when
        normalDistancePolicy.applyPolicy(charge);

        //then
        assertThat(charge.getChargeValue()).isEqualTo(1250);
    }

    @DisplayName("전체 거리가 35km 인 경우")
    @Test
    void applyFirstOverPolicy() {
        //given
        Charge charge = new Charge(1250);
        DistancePolicy normalDistancePolicy = new DistancePolicy(35);

        //when
        normalDistancePolicy.applyPolicy(charge);

        //then
        assertThat(charge.getChargeValue()).isEqualTo(1750);
    }

    @DisplayName("전체 거리가 67km 인 경우")
    @Test
    void applySecondOverPolicy() {
        //given
        Charge charge = new Charge(1250);
        DistancePolicy normalDistancePolicy = new DistancePolicy(67);

        //when
        normalDistancePolicy.applyPolicy(charge);

        //then
        assertThat(charge.getChargeValue()).isEqualTo(2350);
    }
}
