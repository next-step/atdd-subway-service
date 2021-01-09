package nextstep.subway.fare.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("거리별 추가 할인 정보")
public class FareTest {

    @DisplayName("이용 거리초과 시 추가운임 부과")
    @Test
    void calculateDistanceFare() {
        assertThat(new FareDistance(10).getAmountFare()).isEqualTo(1250);
        assertThat(new FareDistance(11).getAmountFare()).isEqualTo(1550);
        assertThat(new FareDistance(20).getAmountFare()).isEqualTo(1650);
        assertThat(new FareDistance(51).getAmountFare()).isEqualTo(1950);
    }

    @DisplayName("연령별 요금 할인")
    @Test
    void calculateAgeFare() {
        assertThat(new FareAge(5).getAmount(2000)).isZero();
        assertThat(new FareAge(7).getAmount(2000)).isEqualTo(1175);
        assertThat(new FareAge(13).getAmount(2000)).isEqualTo(680);
        assertThat(new FareAge(21).getAmount(2000)).isZero();
    }
}
