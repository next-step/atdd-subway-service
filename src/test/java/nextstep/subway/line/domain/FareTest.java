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
        Fare defaultFare = new Fare(Fare.DEFAULT_FARE_AMOUNT);
        //then
        assertThat(fare).isEqualTo(defaultFare);
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
}