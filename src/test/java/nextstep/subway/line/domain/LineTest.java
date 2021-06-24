package nextstep.subway.line.domain;

import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import nextstep.subway.station.domain.Station;

class LineTest {

    Station 강남역 = new Station("강남역");
    Station 광교역 = new Station("광교역");

    @Test
    @DisplayName("노선 생성 및 목록 조회")
    void createLine() {
        // given
        Line 신분당선 = new Line("신분당선", "bg-red-600", 강남역, 광교역, 10);

        //when
        List<Station> stations = 신분당선.getStations();

        //then
        assertThat(stations).containsAll(Arrays.asList(강남역, 광교역));
    }
}