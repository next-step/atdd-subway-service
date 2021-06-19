package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

class LineTest {

    @Test
    void getStation() {
        Station 강남 = new Station("강남");
        Station 광교 = new Station("광교");
        Line sut = new Line("신분당선", "123", 강남, 광교, 10);

        List<Station> stations = sut.getStations();
        assertThat(stations).containsExactly(강남, 광교);

    }

    @Test
    void addSection() {
//        Station 강남 = new Station("강남");
//        Station 광교 = new Station("광교");
//        Line sut = new Line("신분당선", "123", 강남, 광교, 10);
//
//        List<Station> stations = sut.getStation();
//        assertThat(stations).containsExactly(강남, 광교);
//
//        sut.addSection();
    }
}
