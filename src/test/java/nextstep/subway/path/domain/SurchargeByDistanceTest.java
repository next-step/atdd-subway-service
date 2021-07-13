package nextstep.subway.path.domain;

import nextstep.subway.common.domain.SubwayFare;
import nextstep.subway.common.domain.SurchargeByDistance;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class SurchargeByDistanceTest {

    @DisplayName("10km까진 기본요금이다")
    @Test
    void surchargedByDistanceBasicTest() {
        //given
        BigDecimal basicFare = SubwayFare.BASIC_FARE;
        BigDecimal resultFare = BigDecimal.valueOf(1250);
        int distance = 0;

        //when
        BigDecimal chargedFare = SurchargeByDistance.charge(basicFare, distance);

        //then
        assertThat(chargedFare).isEqualTo(resultFare);

        //given
        basicFare = SubwayFare.BASIC_FARE;
        resultFare = BigDecimal.valueOf(1250);
        distance = 10;

        //when
        chargedFare = SurchargeByDistance.charge(basicFare, distance);

        //then
        assertThat(chargedFare).isEqualTo(resultFare);
    }

    @DisplayName("10km~50km까진 5km 당 100원씩 추가된다")
    @Test
    void surchargedByDistanceMediumSectionTest() {
        //given
        BigDecimal basicFare = SubwayFare.BASIC_FARE;
        BigDecimal resultFare = BigDecimal.valueOf(1350);
        int distance = 11;

        //when
        BigDecimal chargedFare = SurchargeByDistance.charge(basicFare, distance);

        //then
        assertThat(chargedFare).isEqualTo(resultFare);


        //given
        basicFare = SubwayFare.BASIC_FARE;
        resultFare = BigDecimal.valueOf(1350);
        distance = 15;

        //when
        chargedFare = SurchargeByDistance.charge(basicFare, distance);

        //then
        assertThat(chargedFare).isEqualTo(resultFare);


        //given
        basicFare = SubwayFare.BASIC_FARE;
        resultFare = BigDecimal.valueOf(2050);
        distance = 50;

        //when
        chargedFare = SurchargeByDistance.charge(basicFare, distance);

        //then
        assertThat(chargedFare).isEqualTo(resultFare);
    }

    @DisplayName("50km 초과부턴 8km 당 100원씩 추가된다")
    @Test
    void surchargedByDistanceOverSectionTest() {
        //given
        BigDecimal basicFare = SubwayFare.BASIC_FARE;
        BigDecimal resultFare = BigDecimal.valueOf(2150);
        int distance = 51;

        //when
        BigDecimal chargedFare = SurchargeByDistance.charge(basicFare, distance);

        //then
        assertThat(chargedFare).isEqualTo(resultFare);


        //given
        basicFare = SubwayFare.BASIC_FARE;
        resultFare = BigDecimal.valueOf(2150);
        distance = 58;

        //when
        chargedFare = SurchargeByDistance.charge(basicFare, distance);

        //then
        assertThat(chargedFare).isEqualTo(resultFare);


        //given
        basicFare = SubwayFare.BASIC_FARE;
        resultFare = BigDecimal.valueOf(2250);
        distance = 59;

        //when
        chargedFare = SurchargeByDistance.charge(basicFare, distance);

        //then
        assertThat(chargedFare).isEqualTo(resultFare);
    }

}