package nextstep.subway.path;

import nextstep.subway.station.domain.Station;
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

@DisplayName("경로 검색 라이브러리 테스트")
public class JgraphTest {
    Station 삼전역 = new Station("삼전역");
    Station 종합운동장역 = new Station("종합운동장역");
    Station 잠실새내역 = new Station("잠실새내역");
    Station 석촌고분 = new Station("석촌고분");
    Station 석촌역 = new Station("석촌역");
    Station 잠실역 = new Station("잠실역");

    private WeightedMultigraph<Station, DefaultWeightedEdge> graph;


    @BeforeEach
    void setUp() {
        graph = getTestGraph();
    }

    @DisplayName("경로 조회 성공 by Dijkstra")
    @Test
    void findPathByDijkstra() {
        /* 삼전역 ----10m----> 종합운동장역 ---10m----> 잠실새내역 */
        /* 삼전역 --1m-->석촌고분역--1m-->석촌역---1m-->잠실역--1m-->잠실새내역 */ // 최단 경로

        DijkstraShortestPath<Station, DefaultWeightedEdge> dijkstraShortestPath = new DijkstraShortestPath<>(graph);
        List<Station> 최단경로 = dijkstraShortestPath.getPath(삼전역, 잠실새내역).getVertexList();
        assertThat(최단경로).containsSequence(삼전역, 석촌고분, 석촌역, 잠실역, 잠실새내역);
    }


    @DisplayName("경로 조회 성공 by KShortest")
    @Test
    public void findPathByKShortest() {
        /* 삼전역 ----10m----> 종합운동장역 ---10m----> 잠실새내역 */
        /* 삼전역 --1m-->석촌고분역--1m-->석촌역---1m-->잠실역--1m-->잠실새내역 */ // 최단 경로

        List<GraphPath> paths = new KShortestPaths(graph, 100).getPaths(삼전역, 잠실새내역);

        assertThat(paths).hasSize(2);
        paths.stream()
            .forEach(it -> {
                assertThat(it.getVertexList()).startsWith(삼전역);
                assertThat(it.getVertexList()).endsWith(잠실새내역);
            });
    }

    private WeightedMultigraph<Station, DefaultWeightedEdge> getTestGraph() {
        WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
        graph.addVertex(삼전역);
        graph.addVertex(종합운동장역);
        graph.addVertex(잠실새내역);
        graph.addVertex(석촌고분);
        graph.addVertex(석촌역);
        graph.addVertex(잠실역);
        graph.setEdgeWeight(graph.addEdge(삼전역, 종합운동장역), 10);
        graph.setEdgeWeight(graph.addEdge(종합운동장역, 잠실새내역), 10);
        graph.setEdgeWeight(graph.addEdge(삼전역, 석촌고분), 1);
        graph.setEdgeWeight(graph.addEdge(석촌고분, 석촌역), 1);
        graph.setEdgeWeight(graph.addEdge(석촌역, 잠실역), 1);
        graph.setEdgeWeight(graph.addEdge(잠실역, 잠실새내역), 1);

        return graph;
    }
}
