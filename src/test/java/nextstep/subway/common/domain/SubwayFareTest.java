package nextstep.subway.common.domain;

import nextstep.subway.exception.IllegalFareException;
import nextstep.subway.exception.NoFavoriteException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
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


    @DisplayName("지하철 요금은 음 수일 순 없다")
    @Test
    void subwaychargedFareCreateFailBecauseOfMinusTest() {

        //when && then
        assertThatThrownBy(() -> new SubwayFare(BigDecimal.valueOf(-1)))
                .isInstanceOf(IllegalFareException.class)
                .hasMessageContaining("지하철 요금은 마이너스가 될 수 없습니다.");

    }

}