package nextstep.subway.line.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class FareTest {

    @DisplayName("요금 생성")
    @Test
    void create() {
        //given
        Fare fare = new Fare();
        //when
        //then
        assertThat(fare).isNotNull();
    }

    @DisplayName("요금 생성 - 값 지정")
    @Test
    void createFeeAmount() {
        //given
        Fare fare = new Fare(100);
        //when
        Fare specifyFare = new Fare(100);
        //then
        assertThat(fare).isEqualTo(specifyFare);
    }

    @DisplayName("요금 비교")
    @Test
    void gt() {
        // given
        Fare fare = new Fare(100);
        Fare mostFare = new Fare(900);
        // when
        Fare expect = fare.gt(mostFare);
        // then
        assertThat(expect).isEqualTo(mostFare);
    }

    @DisplayName("요금 추가 금액 계산 - 10km")
    @Test
    void calculateOverFare() {
        // given
        Fare fare = new Fare();
        // when
        int totalFare = fare.calculateTotalFare(10);
        // then
        assertThat(totalFare).isEqualTo(1250);
    }

    @DisplayName("요금 추가 금액 계산 - 30km")
    @Test
    void calculateOverFareDistance30() {
        // given
        Fare fare = new Fare();
        // when
        int totalFare = fare.calculateTotalFare(30);
        // then
        assertThat(totalFare).isEqualTo(1650);
    }

    @DisplayName("요금 추가 금액 계산 - 50km")
    @Test
    void calculateOverFareDistance50() {
        // given
        Fare fare = new Fare();
        // when
        int totalFare = fare.calculateTotalFare(50);
        // then
        assertThat(totalFare).isEqualTo(2050);
    }

    @DisplayName("요금 추가 금액 계산 - 58km")
    @Test
    void calculateOverFareDistance58() {
        // given
        Fare fare = new Fare();
        // when
        int totalFare = fare.calculateTotalFare(58);
        // then
        assertThat(totalFare).isEqualTo(2150);
    }

    @DisplayName("요금 추가 금액 계산 - 106km")
    @Test
    void calculateOverFareDistance106() {
        // given
        Fare fare = new Fare();
        // when
        int totalFare = fare.calculateTotalFare(106);
        // then
        assertThat(totalFare).isEqualTo(2750);
    }
}