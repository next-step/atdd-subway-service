package nextstep.subway.path;

import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.acceptance.LineAcceptanceTest;
import nextstep.subway.line.acceptance.LineSectionAcceptanceTest;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.station.StationAcceptanceTest;
import nextstep.subway.station.dto.StationResponse;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.alg.shortestpath.KShortestPaths;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@DisplayName("지하철 경로 조회")
public class PathStudyTest extends AcceptanceTest {

    private LineResponse 신분당선;
    private LineResponse 이호선;
    private LineResponse 삼호선;
    private StationResponse 강남역;
    private StationResponse 양재역;
    private StationResponse 교대역;
    private StationResponse 남부터미널역;

    /**
     * 교대역    --- *2호선* ---   강남역
     * |                        |
     * *3호선*                   *신분당선*
     * |                        |
     * 남부터미널역  --- *3호선* ---   양재
     */
    @BeforeEach
    public void setUp() {
        super.setUp();

        강남역 = StationAcceptanceTest.지하철역_등록되어_있음("강남역").as(StationResponse.class);
        양재역 = StationAcceptanceTest.지하철역_등록되어_있음("양재역").as(StationResponse.class);
        교대역 = StationAcceptanceTest.지하철역_등록되어_있음("교대역").as(StationResponse.class);
        남부터미널역 = StationAcceptanceTest.지하철역_등록되어_있음("남부터미널역").as(StationResponse.class);

        신분당선 = LineAcceptanceTest.지하철_노선_등록되어_있음(
                new LineRequest("신분당선", "bg-red-600", 강남역.getId(), 양재역.getId(), 10))
                .as(LineResponse.class);
        이호선 = LineAcceptanceTest.지하철_노선_등록되어_있음(
                new LineRequest("이호선", "bg-red-600", 교대역.getId(), 강남역.getId(), 10))
                .as(LineResponse.class);
        삼호선 = LineAcceptanceTest.지하철_노선_등록되어_있음(
                new LineRequest("삼호선", "bg-red-600", 교대역.getId(), 양재역.getId(), 5))
                .as(LineResponse.class);
        LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청(삼호선, 교대역, 남부터미널역, 3);
        삼호선 = LineAcceptanceTest.지하철_노선_조회_요청(삼호선).as(LineResponse.class);
    }

    @Test
    @DisplayName("출발역에서 도착역으로 가는 모든 경로를 구합니다.")
    public void getAllPath() {
        // given
        // 경로의 출발지 - 도착지
        String 출발역 = 교대역.getName();
        String 도착역 = 양재역.getName();

        WeightedMultigraph<String, DefaultWeightedEdge> graph = new WeightedMultigraph(DefaultWeightedEdge.class);
        // 지하철 역 등록
        graph.addVertex(교대역.getName());
        graph.addVertex(양재역.getName());
        graph.addVertex(강남역.getName());
        graph.addVertex(남부터미널역.getName());

        // 구간 등록
        graph.setEdgeWeight(graph.addEdge(강남역.getName(), 양재역.getName()), 10);
        graph.setEdgeWeight(graph.addEdge(교대역.getName(), 강남역.getName()), 10);
        graph.setEdgeWeight(graph.addEdge(교대역.getName(), 남부터미널역.getName()), 3);
        graph.setEdgeWeight(graph.addEdge(남부터미널역.getName(), 양재역.getName()), 2);

        // when
        List<GraphPath> paths = new KShortestPaths(graph, 10).getPaths(출발역, 도착역);

        // then
        assertThat(paths).hasSize(2);
        paths.stream()
                .forEach(it -> {
                    assertThat(it.getVertexList()).startsWith(출발역);
                    assertThat(it.getVertexList()).endsWith(도착역);
                });
    }

    @Test
    @DisplayName("출발역에서 도착역으로 가는 최단 경로를 구합니다.")
    public void getShortestPath() {
        // given
        // 경로의 출발지 - 도착지
        String 출발역 = 교대역.getName();
        String 도착역 = 양재역.getName();

        WeightedMultigraph<String, DefaultWeightedEdge> graph = new WeightedMultigraph(DefaultWeightedEdge.class);
        // 지하철 역 등록
        graph.addVertex(교대역.getName());
        graph.addVertex(양재역.getName());
        graph.addVertex(강남역.getName());
        graph.addVertex(남부터미널역.getName());

        // 구간 등록
        graph.setEdgeWeight(graph.addEdge(강남역.getName(), 양재역.getName()), 10);
        graph.setEdgeWeight(graph.addEdge(교대역.getName(), 강남역.getName()), 10);
        graph.setEdgeWeight(graph.addEdge(교대역.getName(), 남부터미널역.getName()), 3);
        graph.setEdgeWeight(graph.addEdge(남부터미널역.getName(), 양재역.getName()), 2);

        // when
        DijkstraShortestPath dijkstraShortestPath = new DijkstraShortestPath(graph);
        GraphPath path = dijkstraShortestPath.getPath(출발역, 도착역);
        List<String> shortestPath = path.getVertexList();
        int distance = (int) path.getWeight();

        // then
        assertThat(shortestPath.size()).isEqualTo(3);
        assertThat(distance).isEqualTo(5);
    }
}
