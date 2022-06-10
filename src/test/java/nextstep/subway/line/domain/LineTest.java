package nextstep.subway.line.domain;


import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LineTest {

    private Line line;
    private Station 잠실역;
    private Station 강남역;

    @BeforeEach
    void before() {
        잠실역 = new Station("잠실역");
        강남역 = new Station("강남역");
        line = new Line("2호선", "green", 잠실역, 강남역,10);
    }

    @Test
    void getStations() {
        List<Station> stations = line.getStations();
        assertThat(stations).containsExactly(잠실역, 강남역);
    }

    @Test
    void addSectionTest() {
        Station 선릉역 = new Station("선릉역");
        Section newSection = new Section(잠실역, 선릉역, 5);
        line.addSection(newSection);
        assertThat(line.getStations()).containsExactly(잠실역, 선릉역, 강남역);
    }

    @Test
    void removeStationTest() {
        Station 선릉역 = new Station("선릉역");
        Station 신천역 = new Station("신천역");
        Section newSection = new Section(잠실역, 신천역, 5);
        line.addSection(newSection);
        Section newSection2 = new Section(신천역, 선릉역, 3);
        line.addSection(newSection2);
        line.deleteStation(선릉역);
        assertThat(line.getStations()).containsExactly(잠실역, 신천역, 강남역);
    }
}
