package nextstep.subway.path.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
import nextstep.subway.utils.PathTestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PathFinderTest extends PathTestUtils {

    private PathFinder pathFinder;

    /**
     * (10)
     * 교대역    --- *2호선* ---       강남역
     * |                                |
     * *3호선*(3)                    *신분당선*(10)
     * |                               |
     * 남부터미널역  --- *3호선*(2) ---   양재
     */

    @BeforeEach
    public void setUp() {
        super.setUp();
        List<Station> stations = stationRepository.findAll();
        List<Section> sections = sectionRepository.findAll();
        pathFinder = new PathFinder(stations, sections);
    }

    @Test
    @DisplayName("시작역과 도착역의 최단거리를 구하는 도메인 단위 테스트 : 교대역에서 양재역까지 최단거리")
    void getDijkstraShortestPath() {
        // given
        Station 시작점 = 교대역;
        Station 도착점 = 양재역;

        // when
        List<Station> stations = pathFinder.findShortestPathStations(시작점, 도착점);
        int distance = pathFinder.findShortestPathDistance(시작점, 도착점);

        // then
        assertThat(stations.size()).isEqualTo(3);
        assertThat(distance).isEqualTo(5);
    }
}