package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class LineTest {
    @Test
    @DisplayName("노선에 포함된 역을 가져온다")
    void get_stations() {

        // given
        Station 잠실역 = new Station("잠실역");
        Station 문정역 = new Station("문정역");
        Line line = new Line("신분당선", "bg-red-600", 잠실역, 문정역, 10);

        // when
        List<Station> stations = line.getStations();

        // then
        assertThat(stations).contains(잠실역, 문정역);
    }
}
