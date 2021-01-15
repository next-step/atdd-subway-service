package nextstep.subway.path.domain;

import nextstep.subway.line.application.LineService;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import nextstep.subway.utils.PathTestUtils;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

class PathFinderTest extends PathTestUtils {

    @Mock
    private StationService stationService;

    @Mock
    private LineService lineService;

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
        pathFinder = new PathFinder(stationService, lineService);
    }

    @Test
    @DisplayName("시작역과 도착역의 최단거리를 구하는 도메인 단위 테스트 : 교대역에서 양재역까지 최단거리")
    void getDijkstraSortestPath() {
        // given
        Station 시작점 = 교대역;
        Station 도착점 = 양재역;
        when(stationService.findAll()).thenReturn(stationRepository.findAll());
        when(lineService.findAllSection()).thenReturn(sectionRepository.findAll());

        // when
        DijkstraShortestPath shortestPath = pathFinder.findDijkstraPath();
        List<Station> stations = pathFinder.findShortestPathStations(shortestPath, 시작점, 도착점);
        int distance = pathFinder.findShortestPathDistance(shortestPath, 시작점, 도착점);

        // then
        assertThat(stations.size()).isEqualTo(3);
        assertThat(distance).isEqualTo(5);
    }
}