package nextstep.subway.line.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import nextstep.subway.station.domain.Station;

class SectionsTest {

    private Station station1;
    private Station station2;
    private Station station3;
    private Station station4;
    private Station station5;
    private Station station6;
    private Station station7;
    private Line line;
    private Sections sections;

    @BeforeEach
    void setUp() {
        station1 = new Station("강남역");
        station2 = new Station("교대역");
        station3 = new Station("서초역");
        station4 = new Station("방배역");
        station5 = new Station("사당역");
        station6 = new Station("낙성대역");
        station7 = new Station("서울대입구역");
        line = new Line("2호선", "green");
        sections = new Sections(Arrays.asList(new Section(line, station1, station2, 3)));
    }

    @Test
    @DisplayName("구간 순차적 연결 추가")
    void addSection1() {
        // when
        sections.add(new Section(line, station2, station3, 3));
        sections.add(new Section(line, station3, station4, 3));

        // then
        List<Station> stations = sections.getStations();
        List<Station> targetStations = Arrays.asList(station1, station2, station3, station4);
        assertThat(Arrays.equals(stations.toArray(), targetStations.toArray())).isTrue();
    }

    @Test
    @DisplayName("상행역 기준으로 구간 추가")
    void addSection2() {
        // when
        sections.add(new Section(line, station2, station4, 7));
        sections.add(new Section(line, station2, station3, 3));

        // then
        List<Station> stations = sections.getStations();
        List<Station> targetStations = Arrays.asList(station1, station2, station3, station4);
        assertThat(Arrays.equals(stations.toArray(), targetStations.toArray())).isTrue();
    }

    @Test
    @DisplayName("하행역 기준으로 구간 추가")
    void addSection3() {
        // when
        sections.add(new Section(line, station2, station4, 7));
        sections.add(new Section(line, station3, station4, 3));

        // then
        List<Station> stations = sections.getStations();
        List<Station> targetStations = Arrays.asList(station1, station2, station3, station4);
        assertThat(Arrays.equals(stations.toArray(), targetStations.toArray())).isTrue();
    }

    @Test
    @DisplayName("가장 앞쪽에 구간 추가")
    void addSection4() {
        // when
        sections.add(new Section(line, station3, station1, 3));

        // then
        List<Station> stations = sections.getStations();
        List<Station> targetStations = Arrays.asList(station3, station1, station2);
        assertThat(Arrays.equals(stations.toArray(), targetStations.toArray())).isTrue();
    }

    @Test
    @DisplayName("뒤섞인 구간을 정렬함")
    void sort_sections() {
        // given
        Section section1 = new Section(line, station1, station2, 3);
        Section section2 = new Section(line, station2, station3, 3);
        Section section3 = new Section(line, station3, station4, 3);
        Section section4 = new Section(line, station4, station5, 3);

        // then
        Sections sections1 = new Sections(Arrays.asList(section3, section1, section4, section2));

        // then
        List<Station> stations = sections1.getStations();
        List<Station> targetStations = Arrays.asList(station1, station2, station3, station4, station5);

        assertThat(Arrays.equals(stations.toArray(), targetStations.toArray())).isTrue();
    }

    @Test
    @DisplayName("기존 구간의 길이 이상일 경우 에러")
    void distance_error() {
        // when
        sections.add(new Section(line, station2, station4, 3));

        // then
        assertAll(
                () -> assertThatThrownBy(() -> sections.add(new Section(line, station2, station3, 3)))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessage("역과 역 사이의 거리보다 좁은 거리를 입력해주세요"),
                () -> assertThatThrownBy(() -> sections.add(new Section(line, station3, station4, 3)))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessage("역과 역 사이의 거리보다 좁은 거리를 입력해주세요"),
                () -> assertThatThrownBy(() -> sections.add(new Section(line, station2, station3, 5)))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessage("역과 역 사이의 거리보다 좁은 거리를 입력해주세요"),
                () -> assertThatThrownBy(() -> sections.add(new Section(line, station3, station4, 5)))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessage("역과 역 사이의 거리보다 좁은 거리를 입력해주세요")
        );
    }

    @Test
    @DisplayName("기존 구간의 길이 이상일 경우 에러")
    void duplicate_station_error() {
        // when
        sections.add(new Section(line, station2, station3, 3));

        // then
        assertAll(
                () -> assertThatThrownBy(() -> sections.add(new Section(line, station1, station3, 3)))
                        .isInstanceOf(IllegalStateException.class)
                        .hasMessage("이미 등록된 구간 입니다."),
                () -> assertThatThrownBy(() -> sections.add(new Section(line, station3, station1, 3)))
                        .isInstanceOf(IllegalStateException.class)
                        .hasMessage("이미 등록된 구간 입니다.")
        );
    }

    @Test
    @DisplayName("구간이 기존 구간과 연결되지 않음.")
    void not_connect_section_error() {
        // then
        assertThatThrownBy(() -> sections.add(new Section(line, station4, station5, 3)))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("등록할 수 없는 구간 입니다.");
    }
}
