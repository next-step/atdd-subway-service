package nextstep.subway.line.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import nextstep.subway.station.domain.Station;

import java.util.List;

class SectionsTest {
    Sections sections;

    @BeforeEach
    void setup() {
        sections = new Sections();
    }

    @DisplayName("구간 add")
    @Test
    void add() {
        //given
        //when
        sections.add(new Section());
        //then
        assertThat(sections.isEmpty()).isFalse();
    }

    @DisplayName("구간 일급 컬렉션 빈 값 리턴")
    @Test
    void isEmpty() {
        //given
        //when
        //then
        assertThat(sections.isEmpty()).isTrue();
    }

    @DisplayName("사이즈 구하기")
    @Test
    void size() {
        //given
        //when
        sections.add(new Section());
        //then
        assertThat(sections.size()).isEqualTo(1);
    }

    @DisplayName("구간 추가")
    @Test
    void addLineStation() {
        //given
        Line line = new Line("분당선", "노랑색");
        Station upStation = new Station("선릉역");
        Station downStation = new Station("한티역");
        //when
        sections.addSection(line, upStation, downStation, 5);
        //then
        assertThat(sections.size()).isEqualTo(1);
    }

    @DisplayName("모든 역 찾기")
    @Test
    void getStations() {
        //given
        Line line = new Line("분당선", "노랑색");
        Station upStation = new Station("선릉역");
        Station downStation = new Station("한티역");
        sections.addSection(line, upStation, downStation, 5);
        //when
        List<Station> stations = sections.getStations();
        //then
        assertThat(stations.size()).isEqualTo(2);
        assertThat(stations.get(0)).isEqualTo(upStation);
        assertThat(stations.get(1)).isEqualTo(downStation);
    }

    @DisplayName("역 제거")
    @Test
    void deleteStation() {
        //given
        Line line = new Line("분당선", "노랑색");
        Station upStation = new Station("선릉역");
        Station middleStation = new Station("한티역");
        Station downStation = new Station("복정역");
        sections.addSection(line, upStation, middleStation, 5);
        sections.addSection(line, middleStation, downStation, 5);
        //when
        sections.removeStation(line, downStation);
        List<Station> stations = sections.getStations();
        //then
        assertThat(stations.size()).isEqualTo(2);
        assertThat(stations.get(0)).isEqualTo(upStation);
        assertThat(stations.get(1)).isEqualTo(middleStation);
    }
}