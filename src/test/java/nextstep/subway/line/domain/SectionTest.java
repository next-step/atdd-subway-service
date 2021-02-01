package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class SectionTest {


    private Section section;
    private Station station;
    private Station station2;
    private Station station3;
    private Line line;

    @BeforeEach
    void setUp() {
        station = new Station(1L, "강남역");
        station2 = new Station(2L, "양재역");
        station3 = new Station(3L, "건대입구");
        line = new Line(1L, "2호선", "green", station, station3, 10);
        section = new Section(line, station, station2, 10);
    }

    @Test
    void update_up_station() {
        section.updateUpStation(station3, 5);

        Assertions.assertThat(section.getUpStation()).isEqualTo(station3);
    }

    @Test
    void update_down_station() {
        section.updateDownStation(station3, 5);
        Assertions.assertThat(section.getDownStation()).isEqualTo(station3);
    }
}
