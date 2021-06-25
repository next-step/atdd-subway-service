package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Line;
import nextstep.subway.station.domain.Station;
import org.jgrapht.WeightedGraph;
import org.jgrapht.alg.interfaces.ShortestPathAlgorithm;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class PathFinderTest {

    private ShortestPathAlgorithm shortestPathAlgorithm;
    private WeightedGraph<Station, DefaultWeightedEdge> graph;
    private PathFinder pathFinder;

    private Line 육호선 = new Line("6호선", "갈색");
    private Line 삼호선 = new Line("3호선", "주황색");
    private Station 연신내역 = new Station("연신내역");
    private Station 불광역 = new Station("불광역"); //출발
    private Station 응암역 = new Station("응암역"); //도착

    /*
     *       연신내역ㅡ(5)ㅡ불광역
     *          \         /
     *           (5)    (100)
     *             \    /
     *              응암역
     *
     * */

    @BeforeEach
    void setUp() {
        graph = new WeightedMultigraph<Station, DefaultWeightedEdge>(SectionEdge.class);
        graph.addVertex(불광역);
        graph.addVertex(연신내역);
        graph.addVertex(응암역);

        graph.setEdgeWeight(graph.addEdge(연신내역, 불광역), 5);
        graph.setEdgeWeight(graph.addEdge(연신내역, 응암역), 5);
        graph.setEdgeWeight(graph.addEdge(불광역, 응암역), 100);

        shortestPathAlgorithm = new DijkstraShortestPath(graph);
        pathFinder = new PathFinder(shortestPathAlgorithm, graph);
    }

    @DisplayName("다익스트라 알고리즘 이용하여 최단경로 조회")
    @Test
    void 최단경로_조회_성공_다익스트라_알고리즘() {
        //when
        Path shortestPath = pathFinder.getDijkstraShortestPath(불광역, 응암역);

        //then
        assertThat(shortestPath.getPathStations()).hasSize(3)
                .containsExactly(불광역, 연신내역, 응암역);
        assertThat(shortestPath.getTotalDistance()).isEqualTo(10);
    }
}
