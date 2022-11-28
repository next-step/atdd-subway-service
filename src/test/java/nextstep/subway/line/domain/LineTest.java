package nextstep.subway.line.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class LineTest {

    @DisplayName("노선에 속한 역들을 조회할 수 있다.")
    @Test
    void getStations() {
        Station 강남역 = new Station("강남역");
        Station 광교역 = new Station("광교역");
        Line line = new Line("신분당선", "red", 강남역, 광교역, 10);

        List<Station> stations = line.getStations();

        assertThat(stations).containsExactly(강남역, 광교역);
    }

}
