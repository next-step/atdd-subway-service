package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class LineTest {
    @DisplayName("노선의 역들을 가져온다")
    @Test
    public void getStationsTest() {
        //given
        Station 강남역 = new Station("강남역");
        Station 사당역 = new Station("사당역");
        Line 이호선 = new Line("2호선","green", 강남역, 사당역, 10);

        //when
        List<Station> stations = 이호선.getStations();

        //then
        assertThat(stations).containsExactly(강남역,사당역);
    }
}
