package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class LineTest {

    @DisplayName("노선에 속한 역 목록 조회")
    @Test
    void saveLine() {
        //given
        final Station upStation = new Station("강남역");
        final Station downStation = new Station("광교역");
        final Line line = new Line("신분당선", "bg-red-600", upStation, downStation, 10);

        //when
        final List<Station> stations = line.getStations();

        //then
        assertThat(stations).extracting("name").containsExactly("강남역","광교역");
    }

}