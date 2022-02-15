package nextstep.subway.line.domain;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SectionsTest {

    private static final String SECTION_DISTANCE_EXCEEDED_EXCEPTION = "역과 역 사이의 거리보다 좁은 거리를 입력해주세요";
    private static final String SECTION_ALREADY_EXIST_IN_THE_LINE_EXCEPTION = "이미 등록된 구간 입니다.";
    private static final String STATIONS_NOT_EXIST_IN_THE_LINE_EXCEPTION = "등록할 수 없는 구간 입니다.";
    private static final String SECTION_NOT_FOUND_EXCEPTION = "노선에 역이 포함되지 않습니다.";
    private static final String SECTION_EMPTY_EXCEPTION = "노선은 두개의 역을 포함한 구간이 하나 이상 존재해야 합니다.";

    private Sections sections;

    @BeforeEach
    void setUp() {
        Section firstSection = new Section(LineTest.LINE_2, StationTest.STATION_2, StationTest.STATION_4, 6);
        Section secondSection = new Section(LineTest.LINE_2, StationTest.STATION_4, StationTest.STATION_3, 10);
        sections = new Sections(
            new ArrayList<>(Arrays.asList(firstSection, secondSection)));
    }

    @DisplayName("구간을 생성한다.")
    @Test
    void create_sections() {
        assertThat(sections.getSections().contains(SectionTest.SECTION_2));
        assertThat(sections.getSections().contains(SectionTest.SECTION_1));
    }

    @DisplayName("순서대로 정렬된 역 목록을 조회한다.")
    @Test
    void get_ordered_station() {
        List<Station> orderedStation = sections.getStations();
        assertThat(orderedStation.get(0)).isEqualTo(SectionTest.SECTION_1.getUpStation());
        assertThat(orderedStation.get(1)).isEqualTo(SectionTest.SECTION_2.getUpStation());
        assertThat(orderedStation.get(2)).isEqualTo(SectionTest.SECTION_2.getDownStation());
    }


    @DisplayName("구간 사이에 역을 추가한다.")
    @Test
    void add_inner_section() {
        //given
        int distance = 3;
        int originalDistance = SectionTest.SECTION_2.getDistance();
        Section section = new Section(LineTest.LINE_2, StationTest.STATION_4, StationTest.STATION_1, distance);

        //when
        sections.addSection(section);
        List<Station> stations = sections.getStations();

        //then
        assertThat(sections.getSections().size()).isEqualTo(3);
        assertThat(stations.get(2)).isEqualTo(StationTest.STATION_1);
        assertThat(sections.getSections().stream()
            .filter(s -> s.getUpStation().equals(StationTest.STATION_1)).findFirst().get()
            .getDistance())
            .isEqualTo(originalDistance - distance);
    }

    @DisplayName("상행 종점을 추가한다.")
    @Test
    void add_first_section() {
        //given
        Section section = new Section(LineTest.LINE_2, StationTest.STATION_1, StationTest.STATION_2, 10);

        //when
        sections.addSection(section);

        //then
        assertThat(sections.getSections().size()).isEqualTo(3);
        assertThat(sections.getStations().get(0)).isEqualTo(StationTest.STATION_1);
    }

    @DisplayName("하행 종점을 추가한다.")
    @Test
    void add_last_section() {
        //given
        Section section = new Section(LineTest.LINE_2, StationTest.STATION_3, StationTest.STATION_1, 10);

        //when
        sections.addSection(section);

        //then
        assertThat(sections.getSections().size()).isEqualTo(3);
        assertThat(sections.getStations().get(3)).isEqualTo(StationTest.STATION_1);
    }

    @DisplayName("노선에 이미 존재하는 구간은 동일 노선에 추가할 수 없다.")
    @Test
    void add_section_with_equal_stations_is_invalid() {
        //given
        Section section = new Section(LineTest.LINE_2, StationTest.STATION_2, StationTest.STATION_4, 2);

        //when
        assertThatThrownBy(() -> sections.addSection(section))
            .isInstanceOf(RuntimeException.class)
            .hasMessage(SECTION_ALREADY_EXIST_IN_THE_LINE_EXCEPTION);

    }


    @DisplayName("이미 존재하는 구간의 거리보다 더 큰 거리를 가진 역은 사이에 추가할 수 없다.")
    @Test
    void add_inner_section_with_smaller_distance_is_invalid() {
        //given
        Section section = new Section(LineTest.LINE_2, StationTest.STATION_4, StationTest.STATION_1, 12);

        //when
        assertThatThrownBy(() -> sections.addSection(section))
            .isInstanceOf(RuntimeException.class)
            .hasMessage(SECTION_DISTANCE_EXCEEDED_EXCEPTION);

    }


    @DisplayName("구간의 두개의 역 중 하나는 노선에 존재해야 한다.")
    @Test
    void add_section_with_stations_not_in_the_line_is_invalid() {
        //given
        Section section = new Section(LineTest.LINE_2, StationTest.STATION_1, StationTest.STATION_5, 4);

        //when
        assertThatThrownBy(() -> sections.addSection(section))
            .isInstanceOf(RuntimeException.class)
            .hasMessageContaining(STATIONS_NOT_EXIST_IN_THE_LINE_EXCEPTION);

    }

    @DisplayName("노선 사이의 역을 제거한다.")
    @Test
    void delete_inner_station() {
        //given
        int expectedDistance = 16;

        //when
        sections.removeStation(StationTest.STATION_4);
        Section section = sections.getSections().get(0);

        //then
        assertThat(sections.getSections().size()).isEqualTo(1);
        System.out.println(section.getUpStation().getName());
        System.out.println(section.getDownStation().getName());
        assertThat(section.getUpStation()).isEqualTo(SectionTest.SECTION_1.getUpStation());
        assertThat(section.getDownStation()).isEqualTo(SectionTest.SECTION_2.getDownStation());
        assertThat(section.getDistance()).isEqualTo(expectedDistance);
    }


    @DisplayName("상행 종점을 제거한다.")
    @Test
    void delete_first_Station() {
        //given
        Station expectedFirstStation = StationTest.STATION_4;

        //when
        sections.removeStation(StationTest.STATION_2);
        Station firstStation = sections.getStations().get(0);

        //then
        assertThat(sections.getSections().size()).isEqualTo(1);
        assertThat(firstStation).isEqualTo(expectedFirstStation);
    }

    @DisplayName("하행 종점을 제거한다.")
    @Test
    void delete_last_Station() {
        //given
        Station expectedLastStation = StationTest.STATION_4;

        //when
        sections.removeStation(StationTest.STATION_3);
        Station lastStation = sections.getStations().get(sections.getStations().size() - 1);

        //then
        assertThat(sections.getSections().size()).isEqualTo(1);
        assertThat(lastStation).isEqualTo(expectedLastStation);
    }

    @DisplayName("노선안에 존재하지 않는 역은 삭제할 수 없다.")
    @Test
    void delete_station_not_in_the_line_is_invalid() {
        assertThatThrownBy(() -> sections.removeStation(StationTest.STATION_5))
            .isInstanceOf(RuntimeException.class)
            .hasMessage(SECTION_NOT_FOUND_EXCEPTION);
    }


    @DisplayName("구간이 하나인 노선의 역을 삭제할 수 없다.")
    @Test
    void delete_station_with_one_section_line_is_invalid() {
        //given
        Sections sections = new Sections(new ArrayList<>(Arrays.asList(SectionTest.SECTION_2)));

        //when
        assertThatThrownBy(() -> sections.removeStation(StationTest.STATION_4))
            .isInstanceOf(RuntimeException.class)
            .hasMessage(SECTION_EMPTY_EXCEPTION);
    }

}