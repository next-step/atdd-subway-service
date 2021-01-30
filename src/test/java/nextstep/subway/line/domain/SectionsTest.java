package nextstep.subway.line.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import nextstep.subway.station.domain.Station;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SectionsTest {

    private Sections sections;
    private Station station;
    private Station station2;
    private Station station3;
    private Station station4;
    private Station station5;
    private Line line;
    Section section;
    Section section2;
    Section section3;
    Section section4;

    @BeforeEach
    void setUp() {
        station = new Station(1L, "교대역");
        station2 = new Station(2L, "강남역");
        station3 = new Station(3L, "삼성역");
        station4 = new Station(4L, "선릉역");
        station5 = new Station(5L, "종합운동장");
        line = new Line(1L, "2호선", "green", station, station3, 10);
        section = new Section(1L,line, station, station2, 5);
        section2 = new Section(2L,line, station2, station3, 10);
        section3 = new Section(3L,line, station2, station4, 5);
        section4 = new Section(4L,line, station4, station3, 5);
        List<Section> sections = new ArrayList<>();
        sections.add(section);
        sections.add(section2);
        this.sections = new Sections(sections);
    }

    @Test
    void add() {
        //when
        sections.add(section3);
        //then
        Stations stations = sections.getStations();
        Assertions.assertThat(stations.getStations())
            .extracting(Station::getName)
            .contains(section3.getDownStation().getName());

    }

    @Test
    void noneMatch() {
        //when
        boolean result = sections.noneMatch(station5);
        boolean result2 = sections.noneMatch(station3);
        //then
        Assertions.assertThat(result).isTrue();
        Assertions.assertThat(result2).isFalse();
    }

    @Test
    void anyMatch() {
        //when
        boolean result = sections.anyMatch(station3);
        boolean result2 = sections.anyMatch(station5);
        //then
        Assertions.assertThat(result).isTrue();
        Assertions.assertThat(result2).isFalse();
    }

    @Test
    void findUpStation() {
        //when
        Optional<Section> upStation = sections.findUpStation(section2.getUpStation());
        //then
        Assertions.assertThat(upStation.get().getUpStation()).isEqualTo(section2.getUpStation());
    }

    @Test
    void findDownStation() {
        //when
        Optional<Section> upStation = sections.findDownStation(section2.getDownStation());
        //then
        Assertions.assertThat(upStation.get().getDownStation()).isEqualTo(section2.getDownStation());
    }

    @Test
    void size() {
        //when
        int size = sections.size();
        //then
        Assertions.assertThat(size).isEqualTo(2);
    }

    @Test
    void remove() {
        //when
        sections.remove(section);
        //then
        Assertions.assertThat(sections.size()).isEqualTo(1);
    }
}
