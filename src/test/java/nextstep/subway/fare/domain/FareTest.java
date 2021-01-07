package nextstep.subway.fare.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("거리별 추가 요금 정보")
public class FareTest {

    @DisplayName("이용 거리초과 시 추가운임 부과")
    @Test
    void calculateDistanceFare() {
        assertThat(new FareDistance(10).getAmountFare()).isZero();
        assertThat(new FareDistance(11).getAmountFare()).isEqualTo(300);
        assertThat(new FareDistance(20).getAmountFare()).isEqualTo(400);
        assertThat(new FareDistance(51).getAmountFare()).isEqualTo(700);
    }
}
