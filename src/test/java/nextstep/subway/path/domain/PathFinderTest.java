package nextstep.subway.path.domain;

import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.domain.Line;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("경로를 찾아주는 패스파인더 테스트")
class PathFinderTest extends AcceptanceTest {
    private Station 김포공항역;    // 5호선, 공항철도
    private Station 까치산역;      // 2호선, 5호선
    private Station 교대역;        // 2호선, 3호선
    private Station 강남역;        // 2호선, 신분당선
    private Station 남부터미널역;  // 3호선
    private Station 양재역;        // 3호선, 신분당선
    private Station 영등포역;      // 1호선
    private Station 주안역;        // 1호선

    private Line 일호선;
    private Line 이호선;
    private Line 삼호선;
    private Line 오호선;
    private Line 신분당선;

    private List<Line> lines = new ArrayList<>();

    @BeforeEach
    void setUpParam() {
        김포공항역 = new Station(1L, "김포공항역");
        까치산역 = new Station(2L, "까치산역");
        교대역 = new Station(3L, "교대역");
        강남역 = new Station(4L, "강남역");
        남부터미널역 = new Station(5L, "남부터미널역");
        양재역 = new Station(6L, "양재역");
        영등포역 = new Station(7L, "영등포역");
        주안역 = new Station(8L, "주안역");

        이호선 = new Line("2호선", "#009D3E");
        이호선.addSection(까치산역, 교대역, 16);
        이호선.addSection(교대역, 강남역, 1);
        삼호선 = new Line("삼호선", "#EF7C1C");
        삼호선.addSection(교대역, 남부터미널역, 2);
        삼호선.addSection(남부터미널역, 양재역, 2);
        오호선 = new Line("오호선", "#996CAC");
        오호선.addSection(김포공항역, 까치산역, 8);
        신분당선 = new Line("신분당선", "#D4003B");
        신분당선.addSection(강남역, 양재역, 2);
        일호선 = new Line("1호선", "#0052A4");
        일호선.addSection(영등포역, 주안역, 17);

        lines.add(이호선);
        lines.add(삼호선);
        lines.add(오호선);
        lines.add(신분당선);
        lines.add(일호선);
    }

    @DisplayName("최단 거리 경로를 찾아 유효한 그래프 객체를 반환한다.")
    @Test
    void findShortestPath() {
        final PathFinder pathFinder = PathFinder.of(lines);
        final GraphPath<Station, DefaultWeightedEdge> shortestPath = pathFinder.findShortestPath(김포공항역, 양재역);

        assertThat(shortestPath).isNotNull();
        assertThat(shortestPath.getVertexList()).containsExactlyElementsOf(Arrays.asList(김포공항역, 까치산역, 교대역, 강남역, 양재역));
        assertThat(shortestPath.getWeight()).isEqualTo(27);
    }

    @DisplayName("노선 리스트가 비어있는 경우 에러를 반환한다.")
    @Test
    void createPathFinderWhenGivenEmptyLineList() {
        assertThatThrownBy(() -> PathFinder.of(Collections.emptyList()))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("지하철 노선 데이터가 올바르지 않습니다.");
    }

    @DisplayName("출발역과 도착역이 같을 때 에러를 반환한다.")
    @Test
    void findShortestPathWhenDepartureAndArrivalAreSame() {
        final PathFinder pathFinder = PathFinder.of(lines);

        assertThatThrownBy(() -> pathFinder.findShortestPath(김포공항역, 김포공항역))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("출발역과 도착역은 서로 다른 역이어야 합니다.");
    }

    @DisplayName("출발역에서 도착역으로 이동할 수 없는 경우 에러를 반환한다.")
    @Test
    void findShortestPathWhenCanNotMoveToArrivalFromDeparture() {
        final PathFinder pathFinder = PathFinder.of(lines);

        assertThatThrownBy(() -> pathFinder.findShortestPath(김포공항역, 영등포역))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("출발역에서 도착역으로 이동할 수 없습니다.");
    }
}
