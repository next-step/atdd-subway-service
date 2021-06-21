package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Line;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PathNavigationTest {

    PathNavigation sut;
    private Station 강남;
    private Station 광교;
    private Station 구리;
    private Station 용산;
    private List<Line> lines;

    @BeforeEach
    void setUp() {
        강남 = new Station(1L, "강남");
        광교 = new Station(2L, "광교");
        구리 = new Station(3L, "광교");
        용산 = new Station(4L, "용산");
        lines = new ArrayList<>();
        lines.add(new Line("신분당선", "gs-1123", 강남, 광교, 100));
        lines.add(new Line("중앙선", "gs-1123", 구리, 용산, 200));
    }

    @Test
    void createByLine() {
        List<Line> list = new ArrayList<>();
        sut = PathNavigation.by(list);
        assertThat(sut).isNotNull();
    }

    @Test
    void findShortestPath() {

        sut = PathNavigation.by(lines);

        Path shortestPath = sut.findShortestPath(강남, 광교);

        assertThat(shortestPath.stations()).contains(강남, 광교);
        assertThat(shortestPath.distance()).isEqualTo(100);
    }

    @Test
    void IllegalArgumentException_when_add_EqualsStations() {
        sut = PathNavigation.by(lines);

        assertThatThrownBy(() -> sut.findShortestPath(강남, 강남))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("동일한 역을 입력하였습니다");
    }

    @Test
    void IllegalArgumentException_when_path_is_not_connected() {
        sut = PathNavigation.by(lines);

        assertThatThrownBy(() -> sut.findShortestPath(강남, 용산))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("역이 연결되어 있지 않습니다.");
    }

    @Test
    void IllegalArgumentException_when_path_is_not_registered() {
        sut = PathNavigation.by(lines);
        Station 없는역 = new Station(10L,"없는역");
        assertThatThrownBy(() -> sut.findShortestPath(강남, 없는역))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("존재하지 않은 출발역이나 도착역이 있습니다.");
    }
}