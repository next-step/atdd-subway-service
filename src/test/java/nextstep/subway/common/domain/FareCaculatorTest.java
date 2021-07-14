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
        SubwayFare surcharge = byLineCalculator.calculate(new SubwayFare(0), "신분당선");

        //then
        assertThat(surcharge.charged()).isEqualTo(SurchargeByLine.SINBUNDANG.charge());


        //given && when
        surcharge = byLineCalculator.calculate(new SubwayFare(0), "이호선");

        //then
        assertThat(surcharge.charged()).isEqualTo(SurchargeByLine.NORMAL.charge());
    }

    @DisplayName("거리에에 따라 추가요금이 다르다")
    @Test
    void byDistanceCalculatorTest() {

        //given && when
        SubwayFare chargedFare = new SubwayFare(0);
        FareCaculator byDistanceCalculator = new ByDistanceCalculator();
        SubwayFare surcharge = byDistanceCalculator.calculate(chargedFare, 10);

        //then
        assertThat(surcharge.charged()).isEqualTo(BigDecimal.ZERO);


        //given && when
        surcharge = byDistanceCalculator.calculate(chargedFare, 15);

        //then
        assertThat(surcharge.charged()).isEqualTo(BigDecimal.valueOf(100));

        //given && when
        surcharge = byDistanceCalculator.calculate(chargedFare, 51);

        //then
        assertThat(surcharge.charged()).isEqualTo(BigDecimal.valueOf(900));
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
        SubwayFare chargedFare = new SubwayFare(SubwayFare.BASIC_FARE);
        FareCaculator byAgeCalculator = new ByAgeCalculator();
        SubwayFare surcharge = byAgeCalculator.calculate(chargedFare, 6);

        //then
        assertThat(surcharge.charged()).isEqualTo(유아요금);


        //given && when
        surcharge = byAgeCalculator.calculate(chargedFare, 7);

        //then
        assertThat(surcharge.charged()).isNotEqualTo(유아요금);
        assertThat(surcharge.charged()).isEqualTo(어린이요금);

        //given && when
        surcharge = byAgeCalculator.calculate(chargedFare, 14);

        //then
        assertThat(surcharge.charged()).isNotEqualTo(어린이요금);
        assertThat(surcharge.charged()).isEqualTo(청소년요금);

        //given && when
        surcharge = byAgeCalculator.calculate(chargedFare, 19);

        //then
        assertThat(surcharge.charged()).isNotEqualTo(청소년요금);
        assertThat(surcharge.charged()).isEqualTo(일반요금);


        //given && when
        surcharge = byAgeCalculator.calculate(chargedFare, 65);

        //then
        assertThat(surcharge.charged()).isNotEqualTo(노인요금);
        assertThat(surcharge.charged()).isEqualTo(일반요금);

        //given && when
        surcharge = byAgeCalculator.calculate(chargedFare, 66);

        //then
        assertThat(surcharge.charged()).isNotEqualTo(일반요금);
        assertThat(surcharge.charged()).isEqualTo(노인요금);
    }



}