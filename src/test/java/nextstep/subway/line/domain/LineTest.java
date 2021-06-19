package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.Stations;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class LineTest {

    private Line line;
    private Station 광교;
    private Station 강남;
    private Station 정자;
    private Section section;

    @BeforeEach
    void setUp() {
        line = new Line("신분당선", "1234");
        광교 = new Station("광교");
        강남 = new Station("강남");
        정자 = new Station("정자");
        section = new Section(line, 강남, 광교, 10);
        line.addSection(section);
    }

    @Test
    void addSectionTest() {
        assertThat(line.getSections().getValues()).hasSize(1);
        assertThat(line.getSections().get(0)).isEqualTo(section);
    }

    @Test
    void getStationsTest() {
        Stations expected = Stations.of(Lists.list(강남, 광교));
        assertThat(line.getStations()).isEqualTo(expected);
    }

    @Test
    void updateLineTest() {
        Line updateLine = new Line("변경호선", "gsd-123");
        line.update(updateLine);
        assertThat(line.getName()).isEqualTo(updateLine.getName());
        assertThat(line.getColor()).isEqualTo(updateLine.getColor());
    }

    @Test
    void removeLineStationTest() {
        Section section = new Section(line, 강남, 정자, 5);
        line.addSection(section);

        line.removeLineStation(정자);

        assertThat(line.getStations().getValues()).isEqualTo(Lists.list(강남, 광교));
    }
}
