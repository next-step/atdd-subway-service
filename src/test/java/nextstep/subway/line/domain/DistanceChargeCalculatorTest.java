package nextstep.subway.line.domain;

import static org.assertj.core.api.Assertions.*;

import nextstep.subway.line.application.DistanceChargeCalculator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class DistanceChargeCalculatorTest {

    @Test
    @DisplayName("거리 할인 없는 경우 : 10km 이내")
    void calculate1(){
        // given
        int charge = 1250;
        int distance = 10;

        // when
        int result = DistanceChargeCalculator.calculate(charge, distance);

        // then
        assertThat(result).isEqualTo(1250);
    }

    @Test
    @DisplayName("거리 할인 있는 경우 : 10km ~ 50km")
    void calculate2(){
        // given
        int charge = 1250;
        int distance = 50;

        // when
        int result = DistanceChargeCalculator.calculate(charge, distance);

        // then
        assertThat(result).isEqualTo(2050);
    }

    @Test
    @DisplayName("거리 할인 있는 경우 : 50km 초과")
    void calculate3(){
        // given
        int charge = 1250;
        int distance = 100;

        // when
        int result = DistanceChargeCalculator.calculate(charge, distance);

        // then
        assertThat(result).isEqualTo(2750);
    }
}
