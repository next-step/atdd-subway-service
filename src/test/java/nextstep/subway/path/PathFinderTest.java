package nextstep.subway.path;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import nextstep.subway.line.domain.Line;
import nextstep.subway.path.domain.Path;
import nextstep.subway.path.domain.PathFinder;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PathFinderTest {
    private PathFinder pathFinder = new PathFinder();

    private List<Line> 전노선;
    private Line 일호선;
    private Line 사호선;
    private Station 서울역;
    private Station 사당역;
    private Station 동대문역;

    @BeforeEach
    void makeDefaultLine() {
        서울역 = new Station("서울역");
        사당역 = new Station("사당역");
        동대문역 = new Station("동대문역");

        일호선 = new Line("1호선", "navy", 서울역, 사당역, 10);
        사호선 = new Line("4호선", "sky-blue", 동대문역, 사당역, 8);

        전노선 = new ArrayList<>();
        전노선.add(일호선);
        전노선.add(사호선);
    }

    @DisplayName("두 역간의 최단거리 조회")
    @Test
    void findShortestPath() {
        Path path = pathFinder.findShortestPath(전노선, 서울역, 동대문역);

        assertThat(path.getStations()).containsExactly(서울역, 사당역, 동대문역);
        assertThat(path.getDistance()).isEqualTo(18);
    }

    @DisplayName("최단거리 조회시 출발역과 도착역이 같을 경우")
    @Test
    void findShortestPathWithSameStation() {
        assertThatThrownBy(() -> pathFinder.findShortestPath(전노선, 서울역, 서울역)).isInstanceOf(
                IllegalArgumentException.class);
    }

    @DisplayName("최단거리 조회시 노선상에 등록되지 않은 역에 대해 조회할 경우")
    @Test
    void findShortestPathWithUnknownStation() {
        Station 도쿄역 = new Station("도쿄역");
        assertThatThrownBy(() -> pathFinder.findShortestPath(전노선, 서울역, 도쿄역)).isInstanceOf(
                IllegalArgumentException.class);
    }

    @DisplayName("최단거리 조회시 서로 연결되어 있지 않은 역에 대해 조회할 경우")
    @Test
    void findShortestPathWithNotConnectedStation() {
        Station 서면역 = new Station("서면역");
        Station 부산역 = new Station("부산역");

        Line 부산선 = new Line("부산선", "blue", 서면역, 부산역, 21);
        전노선.add(부산선);

        assertThatThrownBy(() -> pathFinder.findShortestPath(전노선, 부산역, 서울역)).isInstanceOf(NoSuchElementException.class);
    }
}
