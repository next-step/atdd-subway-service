package nextstep.subway.line.domain;

import static org.assertj.core.api.Assertions.assertThat;

import nextstep.subway.station.domain.Station;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LineTest {

    private Station station;
    private Station station2;
    private Station station3;
    private Line line;

    @BeforeEach
    void setUp() {
        station = new Station(1L, "강남역");
        station2 = new Station(2L, "양재역");
        station3 = new Station(3L, "건대입구");
        line = new Line(1L,"2호선", "green", station, station3, 10);
    }

    @Test
    void get_stations() {
        //when
        Stations stations = line.getStations();
        //then
        assertThat(stations.getStations())
            .extracting(Station::getName)
            .containsExactly(station.getName(),station3.getName());
    }

    @Test
    void add_section(){
        //when
        line.addSection(new Section(line, station, station2, 5));
        //then
        Stations stations = line.getStations();
        assertThat(stations.getStations())
            .extracting(Station::getName)
            .contains(station2.getName());
    }

    @Test
    void remove_station_test() {
        //given
        line.addSection(new Section(line, station, station2, 5));
        //when
        line.removeStation(station2);
        //then
        assertThat(line.getStations().getStations())
            .extracting(Station::getName)
            .noneMatch(s -> s.equals(station2.getName()));

    }


}
