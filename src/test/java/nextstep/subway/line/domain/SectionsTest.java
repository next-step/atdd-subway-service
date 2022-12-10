package nextstep.subway.line.domain;

import static org.assertj.core.api.Assertions.*;

import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import nextstep.subway.station.domain.Station;

class SectionsTest {

    private Line line;
    private Station upStation;
    private Station downStation;
    private Station middleStation;

    @BeforeEach
    void init() {
        this.line = new Line("2노선", "green");
        this.upStation = new Station("교대역");
        this.middleStation = new Station("강남역");
        this.downStation = new Station("역삼역");
    }

    @DisplayName("구간이 0개 일 때 빈 리스트 반환")
    @Test
    void getStations1() {
        Sections sections = new Sections();

        List<Station> stations = sections.getStations();

        assertThat(stations).hasSize(0);
    }

    @DisplayName("구간이 1개 일 때 상행역/하행역 반환")
    @Test
    void getStations2() {
        Sections sections = new Sections();
        sections.add(new Section(line, upStation, downStation, new Distance(5)));

        List<Station> stations = sections.getStations();

        assertThat(stations).hasSize(2);
        assertThat(stations.stream().map(Station::getName).collect(Collectors.toList())).containsExactly("교대역", "역삼역");
    }

    @DisplayName("구간 중간에 새로운 구간 추가")
    @Test
    void addSection1() {
        Sections sections = new Sections();
        sections.add(new Section(line, upStation, downStation, new Distance(5)));

        sections.add(new Section(line, upStation, middleStation, new Distance(3)));

        assertThat(sections.getStations()).hasSize(3);
        assertThat(sections.getStations().stream().map(Station::getName).collect(Collectors.toList())).containsExactly(
            "교대역", "강남역", "역삼역");
    }

    @DisplayName("구간 상행역 앞에 새로운 구간 추가")
    @Test
    void addSection2() {
        Sections sections = new Sections();
        sections.add(new Section(line, middleStation, downStation, new Distance(5)));

        sections.add(new Section(line, upStation, middleStation, new Distance(3)));

        assertThat(sections.getStations()).hasSize(3);
        assertThat(sections.getStations().stream().map(Station::getName).collect(Collectors.toList())).containsExactly(
            "교대역", "강남역", "역삼역");
    }

    @DisplayName("구간 하행역 뒤에 새로운 구간 추가")
    @Test
    void addSection3() {
        Sections sections = new Sections();
        sections.add(new Section(line, upStation, middleStation, new Distance(5)));

        sections.add(new Section(line, middleStation, downStation, new Distance(3)));

        assertThat(sections.getStations()).hasSize(3);
        assertThat(sections.getStations().stream().map(Station::getName).collect(Collectors.toList())).containsExactly(
            "교대역", "강남역", "역삼역");
    }

    @DisplayName("신규 구간의 역이 이미 구간들에 모두 등록된 역일 때")
    @Test
    void addSection4() {
        Sections sections = new Sections();
        sections.add(new Section(line, upStation, downStation, new Distance(5)));

        assertThatThrownBy(() -> sections.add(new Section(line, downStation, upStation, new Distance(3))))
            .isInstanceOf(RuntimeException.class)
            .hasMessage("이미 등록된 구간 입니다.");
    }

    @DisplayName("신규 구간의 역 모두 기존 구간에 존재하지 않는 역일 때")
    @Test
    void addSection5() {
        Sections sections = new Sections();
        sections.add(new Section(line, upStation, downStation, new Distance(5)));

        assertThatThrownBy(
            () -> sections.add(new Section(line, new Station("방배역"), new Station("잠실역"), new Distance(3))))
            .isInstanceOf(RuntimeException.class)
            .hasMessage("등록할 수 없는 구간 입니다.");
    }

    @DisplayName("구간 중간의 역 삭제")
    @Test
    void removeSection1() {
        Sections sections = new Sections();
        sections.add(new Section(line, upStation, downStation, new Distance(5)));
        sections.add(new Section(line, upStation, middleStation, new Distance(3)));

        sections.remove(middleStation);

        assertThat(sections.getStations()).hasSize(2);
        assertThat(sections.getStations().stream().map(Station::getName).collect(Collectors.toList())).containsExactly(
            "교대역", "역삼역");
    }

    @DisplayName("구간의 상행역 삭제")
    @Test
    void removeSection2() {
        Sections sections = new Sections();
        sections.add(new Section(line, upStation, downStation, new Distance(5)));
        sections.add(new Section(line, upStation, middleStation, new Distance(3)));

        sections.remove(upStation);

        assertThat(sections.getStations()).hasSize(2);
        assertThat(sections.getStations().stream().map(Station::getName).collect(Collectors.toList())).containsExactly(
            "강남역", "역삼역");
    }

    @DisplayName("구간의 하행역 삭제")
    @Test
    void removeSection3() {
        Sections sections = new Sections();
        sections.add(new Section(line, upStation, downStation, new Distance(5)));
        sections.add(new Section(line, upStation, middleStation, new Distance(3)));

        sections.remove(downStation);

        assertThat(sections.getStations()).hasSize(2);
        assertThat(sections.getStations().stream().map(Station::getName).collect(Collectors.toList())).containsExactly(
            "교대역", "강남역");
    }

    @DisplayName("1개의 구간만 있을 때 역을 삭제할 수 없다")
    @Test
    void removeSection4() {
        Sections sections = new Sections();
        sections.add(new Section(line, upStation, downStation, new Distance(5)));

        assertThatThrownBy(() -> sections.remove(upStation))
            .isInstanceOf(RuntimeException.class);
    }

}
