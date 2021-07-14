package nextstep.subway.common.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class FareCaculatorTest {

    @DisplayName("노선에 따라 추가요금이 다르다")
    @Test
    void byLineCalculatorTest() {

        //given && when
        FareCaculator byLineCalculator = new ByLineCalculator();
        BigDecimal surcharge = byLineCalculator.calculate(BigDecimal.ZERO, "신분당선");

        //then
        assertThat(surcharge).isEqualTo(SurchargeByLine.SINBUNDANG.charge());


        //given && when
        surcharge = byLineCalculator.calculate(BigDecimal.ZERO, "이호선");

        //then
        assertThat(surcharge).isEqualTo(SurchargeByLine.NORMAL.charge());
    }

    @DisplayName("거리에에 따라 추가요금이 다르다")
    @Test
    void byDistanceCalculatorTest() {

        //given && when
        BigDecimal chargedFare = BigDecimal.ZERO;
        FareCaculator byDistanceCalculator = new ByDistanceCalculator();
        BigDecimal surcharge = byDistanceCalculator.calculate(chargedFare, 10);

        //then
        assertThat(surcharge).isEqualTo(BigDecimal.ZERO);


        //given && when
        surcharge = byDistanceCalculator.calculate(chargedFare, 15);

        //then
        assertThat(surcharge).isEqualTo(BigDecimal.valueOf(100));

        //given && when
        surcharge = byDistanceCalculator.calculate(chargedFare, 51);

        //then
        assertThat(surcharge).isEqualTo(BigDecimal.valueOf(900));
    }

    @DisplayName("나이에에 따라 요금이 할인된다")
    @Test
    void byAgeCalculatorTest() {

        //given && when
        //유아 요금할인을 받을 수 있다
        BigDecimal 유아요금 = BigDecimal.ZERO;
        BigDecimal 어린이요금 = BigDecimal.valueOf(450);
        BigDecimal 청소년요금 = BigDecimal.valueOf(720);
        BigDecimal 일반요금 = BigDecimal.valueOf(1250);
        BigDecimal 노인요금 = BigDecimal.ZERO;
        BigDecimal chargedFare =SubwayFare.BASIC_FARE;
        FareCaculator byAgeCalculator = new ByAgeCalculator();
        BigDecimal surcharge = byAgeCalculator.calculate(chargedFare, 6);

        //then
        assertThat(surcharge).isEqualTo(유아요금);


        //given && when
        surcharge = byAgeCalculator.calculate(chargedFare, 7);

        //then
        assertThat(surcharge).isNotEqualTo(유아요금);
        assertThat(surcharge).isEqualTo(어린이요금);

        //given && when
        surcharge = byAgeCalculator.calculate(chargedFare, 14);

        //then
        assertThat(surcharge).isNotEqualTo(어린이요금);
        assertThat(surcharge).isEqualTo(청소년요금);

        //given && when
        surcharge = byAgeCalculator.calculate(chargedFare, 19);

        //then
        assertThat(surcharge).isNotEqualTo(청소년요금);
        assertThat(surcharge).isEqualTo(일반요금);


        //given && when
        surcharge = byAgeCalculator.calculate(chargedFare, 65);

        //then
        assertThat(surcharge).isNotEqualTo(노인요금);
        assertThat(surcharge).isEqualTo(일반요금);

        //given && when
        surcharge = byAgeCalculator.calculate(chargedFare, 66);

        //then
        assertThat(surcharge).isNotEqualTo(일반요금);
        assertThat(surcharge).isEqualTo(노인요금);
    }



}