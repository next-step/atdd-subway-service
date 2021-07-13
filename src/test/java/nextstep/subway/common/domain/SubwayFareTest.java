package nextstep.subway.common.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class SubwayFareTest {

    @DisplayName("지하철 기본요금은 1250원이다")
    @Test
    void subwayBasicFareTest() {
        //given
        SubwayFare subwayFare = new SubwayFare();

        //when
        BigDecimal charedFare = subwayFare.charged();

        //then
        assertThat(charedFare).isEqualTo(BigDecimal.valueOf(1250));
    }

    @DisplayName("지하철 기본요금을 지정할 수 있다")
    @Test
    void subwaychargedFareTest() {
        //given
        SubwayFare subwayFare = new SubwayFare(BigDecimal.valueOf(1000));

        //when
        BigDecimal charedFare = subwayFare.charged();

        //then
        assertThat(charedFare).isEqualTo(BigDecimal.valueOf(1000));
    }

}