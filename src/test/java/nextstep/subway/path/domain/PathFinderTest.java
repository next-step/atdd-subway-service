package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Lines;
import nextstep.subway.line.domain.SectionGraph;
import nextstep.subway.member.domain.MemberAge;
import nextstep.subway.station.domain.Station;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class PathFinderTest {
    private Station 강남역;
    private Station 양재역;
    private Station 교대역;
    private Station 남부터미널역;

    @BeforeEach
    void setUp() {
        강남역 = new Station("강남역");
        양재역 = new Station("양재역");
        교대역 = new Station("교대역");
        남부터미널역 = new Station("남부터미널역");
    }

    @DisplayName("pathFinder 생성 테스트")
    @Test
    public void createPathFinder() throws Exception {
        // given
        Line 신분당선 = new Line("신분당선", "red", 강남역, 양재역, 5, 500);
        Line 이호선 = new Line("이호선", "green", 교대역, 강남역, 10, 500);
        Line 삼호선 = new Line("삼호선", "orange", 교대역, 양재역, 20, 0);
        삼호선.addSection(교대역, 남부터미널역, 2);
        Lines lines  = new Lines(Arrays.asList(신분당선, 이호선, 삼호선));

        // when
        SectionGraph graph = new SectionGraph();
        PathFinder pathFinder = new PathFinder(lines.createPath(graph));
        Path 교대역_양재역_경로조회_결과 = pathFinder.findPath(교대역, 양재역, new MemberAge(20));
        Path 강남역_양재역_경로조회_결과 = pathFinder.findPath(강남역, 양재역, new MemberAge(20));
        Path 강남역_양재역_어린이_경로조회_결과 = pathFinder.findPath(강남역, 양재역, new MemberAge(12));
        Path 강남역_양재역_청소년_경로조회_결과 = pathFinder.findPath(강남역, 양재역, new MemberAge(13));

        // then
        assertAll(
                () -> assertThat(지하철역_이름_조회(교대역_양재역_경로조회_결과.getStations())).containsExactly("교대역", "강남역", "양재역"),
                () -> assertThat(교대역_양재역_경로조회_결과.getDistance()).isEqualTo(15),
                () -> assertThat(교대역_양재역_경로조회_결과.getFare()).isEqualTo(1250 + 300 + 1000),
                () -> assertThat(강남역_양재역_경로조회_결과.getFare()).isEqualTo(1250 + 500),
                () -> assertThat(강남역_양재역_어린이_경로조회_결과.getFare()).isEqualTo((int) ((1250 + 500 - 350) * 0.5 + 350)),
                () -> assertThat(강남역_양재역_청소년_경로조회_결과.getFare()).isEqualTo((int) ((1250 + 500 - 350) * 0.8 + 350))
        );
    }

    private List<String> 지하철역_이름_조회(List<Station> stations) {
        return stations.stream().map(Station::getName).collect(Collectors.toList());
    }

    @DisplayName("jgrapht 라이브러리 테스트")
    @Test
    public void getDijkstraShortestPath() {
        WeightedMultigraph<String, DefaultWeightedEdge> graph = new WeightedMultigraph(DefaultWeightedEdge.class);
        graph.addVertex("v1");
        graph.addVertex("v2");
        graph.addVertex("v3");
        graph.setEdgeWeight(graph.addEdge("v1", "v2"), 2);
        graph.setEdgeWeight(graph.addEdge("v2", "v3"), 2);
        graph.setEdgeWeight(graph.addEdge("v1", "v3"), 100);

        DijkstraShortestPath dijkstraShortestPath = new DijkstraShortestPath(graph);
        List<String> shortestPath = dijkstraShortestPath.getPath("v3", "v1").getVertexList();

        assertThat(shortestPath.size()).isEqualTo(3);
    }

    @DisplayName("지하철역 기준으로 jgrapht 라이브러리 테스트")
    @Test
    public void getDijkstraShortestPathOfStation() {
        /**
         * 교대역    --- 10 ---   강남역
         * |                        |
         * 2                        5
         * |                        |
         * 남부터미널역  --- 3 ---   양재
         */

        Station 강남역 = new Station("강남역");
        Station 양재역 = new Station("양재역");
        Station 교대역 = new Station("교대역");
        Station 남부터미널역 = new Station("남부터미널역");

        WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph(DefaultWeightedEdge.class);
        graph.addVertex(강남역);
        graph.addVertex(양재역);
        graph.addVertex(교대역);
        graph.addVertex(남부터미널역);

        graph.setEdgeWeight(graph.addEdge(교대역, 강남역), 10);
        graph.setEdgeWeight(graph.addEdge(교대역, 남부터미널역), 2);
        graph.setEdgeWeight(graph.addEdge(남부터미널역, 양재역), 3);
        graph.setEdgeWeight(graph.addEdge(강남역, 양재역), 5);

        DijkstraShortestPath dijkstraShortestPath = new DijkstraShortestPath(graph);
        List<Station> shortestPath = dijkstraShortestPath.getPath(교대역, 양재역).getVertexList();

        assertThat(shortestPath).containsExactly(교대역, 남부터미널역, 양재역);
    }
}
