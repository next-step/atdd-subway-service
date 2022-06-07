package nextstep.subway.line;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.line.domain.Sections;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("구간 일급컬렉션 단위테스트")
class SectionsTest {
    Station upStation;
    Station downStation;
    Line line;
    Sections sections;

    @BeforeEach
    void setUp() {
        upStation = new Station("상행선");
        downStation = new Station("하행선");
        line = new Line("2호선", "green");
        sections = new Sections();
        sections.add(new Section(line, upStation, downStation, 10));
    }

    @DisplayName("상행종점역을 찾는다")
    @Test
    void findUpStation() {
        Station newStation = new Station("신규역");
        sections.add(new Section(line, newStation, upStation, 5));
        assertThat(sections.findUpStation()).isEqualTo(newStation);
    }

    @DisplayName("구간을 등록하고 노선의 역을 순서대로 찾는다")
    @Test
    void getStations() {
        Station newStation = new Station("신규역");
        sections.add(new Section(line, newStation, upStation, 5));
        assertThat(sections.getStations()).containsExactly(newStation, upStation, downStation);
    }

    @DisplayName("신규역을 상행선과 하행선 사이에 추가한다")
    @Test
    void addStation() {
        Station newStation = new Station("신규역");
        sections.add(new Section(line, newStation, downStation, 5));
        assertThat(sections.getStations()).containsExactly(upStation, newStation, downStation);
    }

    @DisplayName("구간을 삭제한다")
    @Test
    void removeStation() {
        Station newStation = new Station("신규역");
        sections.add(new Section(line, newStation, upStation, 5));
        sections.remove(upStation);
        assertThat(sections.getStations()).containsExactly(newStation, downStation);
    }

    @DisplayName("지하철 노선에 여러개의 역을 순서 상관 없이 등록한다.")
    @Test
    void addStation2() {
        Station newStation1 = new Station("신규역1");
        Station newStation2 = new Station("신규역2");
        sections.add(new Section(line, upStation, newStation1, 2));
        sections.add(new Section(line, newStation2, upStation, 5));
        assertThat(sections.getStations()).containsExactly(newStation2, upStation, newStation1, downStation);
    }

    @DisplayName("지하철 노선에 이미 등록되어있는 역을 등록한다.")
    @Test
    void addStationWithSameStation() {
        assertThatThrownBy(() -> sections.add(new Section(line, upStation, downStation, 2)))
                .isInstanceOf(RuntimeException.class);
    }

    @DisplayName("지하철 노선에 등록되지 않은 역을 기준으로 등록한다.")
    @Test
    void addStationWithNoStation() {
        Station newStation1 = new Station("신규역1");
        Station newStation2 = new Station("신규역2");
        assertThatThrownBy(() -> sections.add(new Section(line, newStation1, newStation2, 2)))
                .isInstanceOf(RuntimeException.class);
    }

    @DisplayName("지하철 노선에 등록된 지하철역이 두개일 때 한 역을 제외한다.")
    @Test
    void removeStation2() {
        assertThatThrownBy(() -> sections.remove(upStation))
                .isInstanceOf(RuntimeException.class);
    }
}
