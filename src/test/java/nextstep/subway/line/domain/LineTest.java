package nextstep.subway.line.domain;

import static org.assertj.core.api.Assertions.assertThat;

import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class LineTest {

    @DisplayName("노선에 추가 요금을 설정한다.")
    @Test
    public void changeFare() {
        //given
        Station 강남역 = new Station("강남역");
        Station 교대역 = new Station("교대역");
        Line 이호선 = new Line("2호선", "green lighten-1", 교대역, 강남역, 10);

        //when
        이호선.changeFare(1000);

        //then
        assertThat(이호선.getFare()).isEqualTo(Fare.of(1000));
    }
}
