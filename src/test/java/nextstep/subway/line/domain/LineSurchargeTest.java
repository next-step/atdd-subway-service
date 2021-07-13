package nextstep.subway.line.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("노선 추가요금 관련")
class LineSurchargeTest {

    @DisplayName("추가요금이 부여된 노선이면 추가요금을 가져올 수 있다")
    @Test
    void surchargePriceTest() {
        //when
        BigDecimal 신분당선_추가요금 = LineSurcharge.price("신분당선");

        //then
        assertThat(신분당선_추가요금).isEqualTo(BigDecimal.valueOf(1000));

        //when
        BigDecimal 일반노선_추가요금 = LineSurcharge.price("이호선");

        //then
        assertThat(일반노선_추가요금).isEqualTo(BigDecimal.ZERO);
    }

}