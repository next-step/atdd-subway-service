package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.path.exception.InvalidFindShortestPathException;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PathFinderTest {
    private Station 강남역;
    private Station 양재역;
    private Station 교대역;
    private Station 남부터미널역;
    private Station 신도림역;
    private Station 영등포역;
    private PathFinder pathFinder;

    /**
     * 1호선: 0원
     * 2호선: 200원
     * 3호선: 300원
     * 신분당선: 500원
     *
     * 신도림역 ---- *1호선*(5) ---- 영등포역
     *
     * 교대역 --- *2호선*(51) ---  강남역
     * |                        |
     * *3호선*(3)              *신분당선*(39)
     * |                        |
     * 남부터미널역 -- *3호선*(9) -- 양재역
     */
    @BeforeEach
    void setUp() {
        강남역 = new Station("강남역");
        양재역 = new Station("양재역");
        교대역 = new Station("교대역");
        남부터미널역 = new Station("남부터미널역");
        신도림역 = new Station("신도림역");
        영등포역 = new Station("영등포역");

        Line 신분당선 = new Line("신분당선", "bg-red-600", 강남역, 양재역, 39, 500);
        Line 일호선 = new Line("일호선", "bg-blue-600", 신도림역, 영등포역, 5, 0);
        Line 이호선 = new Line("이호선", "bg-red-600", 교대역, 강남역, 51, 200);
        Line 삼호선 = new Line("삼호선", "bg-red-600", 교대역, 양재역, 12, 300);
        삼호선.addSection(new Section(삼호선, 교대역, 남부터미널역, new Distance(3)));

        StationGraph stationGraph = new StationGraph(Arrays.asList(신분당선, 일호선, 이호선, 삼호선));
        PathAlgorithm pathAlgorithm = new DijkstraPath(stationGraph.generateGraph());
        pathFinder = new PathFinder(stationGraph, pathAlgorithm);
    }

    @DisplayName("최단경로 조회")
    @Test
    void findShortestPath() {
        Path path = pathFinder.findShortestPath(교대역, 양재역);
        assertThat(path.getStations()).containsExactlyElementsOf(Arrays.asList(교대역, 남부터미널역, 양재역));
        assertThat(path.getDistance()).isEqualTo(12);
    }

    @DisplayName("출발역과 도착역이 같은 경우 조회하지 못한다.")
    @Test
    void findShortestPathException1() {
        assertThatThrownBy(() -> pathFinder.findShortestPath(교대역, 교대역))
                .isInstanceOf(InvalidFindShortestPathException.class)
                .hasMessage("출발역과 도착역이 같으면 조회 불가능합니다.");
    }

    @DisplayName("출발역과 도착역이 연결되어 있지 않은 경우, 조회하지 못한다.")
    @Test
    void findShortestPathException2() {
        assertThatThrownBy(() -> pathFinder.findShortestPath(교대역, 신도림역))
                .isInstanceOf(InvalidFindShortestPathException.class)
                .hasMessage("출발역과 도착역이 연결이 되어 있지 않습니다.");
    }

    @DisplayName("존재하지 않은 출발역이나 도착역을 조회할 경우, 조회하지 못한다.")
    @Test
    void findShortestPathException3() {
        assertThatThrownBy(() -> pathFinder.findShortestPath(교대역, new Station("노량진역")))
                .isInstanceOf(InvalidFindShortestPathException.class)
                .hasMessage("출발역이나 도착역이 존재하지 않습니다.");
    }

    @DisplayName("10km 이하 지하철 이용 요금 조회")
    @Test
    void findShortestPathFare1() {
        Path path = pathFinder.findShortestPath(신도림역, 영등포역);
        assertThat(path.getFare()).isEqualTo(1250);
    }

    @DisplayName("10km 초과 지하철 이용 요금 조회")
    @Test
    void findShortestPathFare2() {
        Path path = pathFinder.findShortestPath(교대역, 양재역);
        assertThat(path.getFare()).isEqualTo(1650);
    }

    @DisplayName("50km 초과 지하철 이용 요금 조회")
    @Test
    void findShortestPathFare3() {
        Path path = pathFinder.findShortestPath(교대역, 강남역);
        assertThat(path.getFare()).isEqualTo(2350);
    }

    @DisplayName("(거리 + 가장 높은 노선별 금액) 요금 조회")
    @Test
    void findShortestPathFare4() {
        Path path = pathFinder.findShortestPath(남부터미널역, 강남역);
        assertThat(path.getFare()).isEqualTo(2350);
    }
}
