package nextstep.subway.line.domain;

import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import nextstep.subway.station.domain.Station;

class LineTest {

    Station 강남역 = new Station("강남역");
    Station 양재역 = new Station("양재역");
    Station 정자역 = new Station("정자역");
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

    @DisplayName("지하철 구간을 등록한다.")
    @Test
    void addLineSection() {
        // given
        Line 신분당선 = new Line("신분당선", "bg-red-600", 강남역, 광교역, 10);
        신분당선.addStation(강남역, 양재역, 3);

        // when
        List<Station> stations = 신분당선.getStations();

        // then
        assertThat(stations).containsAll(Arrays.asList(강남역, 양재역, 광교역));
    }
}