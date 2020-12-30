package study.jgraph;

import nextstep.subway.line.domain.Line;
import nextstep.subway.station.StationFixtures;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.interfaces.ShortestPathAlgorithm;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.alg.shortestpath.KShortestPaths;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

public class JgraphTest {
    @Test
    public void getDijkstraShortestPath() {
        String source = "v3";
        String target = "v1";
        WeightedMultigraph<String, DefaultWeightedEdge> graph = new WeightedMultigraph(DefaultWeightedEdge.class);
        graph.addVertex("v1");
        graph.addVertex("v2");
        graph.addVertex("v3");
        graph.setEdgeWeight(graph.addEdge("v1", "v2"), 2);
        graph.setEdgeWeight(graph.addEdge("v2", "v3"), 2);
        graph.setEdgeWeight(graph.addEdge("v1", "v3"), 100);

        DijkstraShortestPath dijkstraShortestPath = new DijkstraShortestPath(graph);
        List<String> shortestPath = dijkstraShortestPath.getPath(source, target).getVertexList();

        assertThat(shortestPath.size()).isEqualTo(3);
    }

    @Test
    public void getKShortestPaths() {
        String source = "v3";
        String target = "v1";

        WeightedMultigraph<String, DefaultWeightedEdge> graph = new WeightedMultigraph(DefaultWeightedEdge.class);
        graph.addVertex("v1");
        graph.addVertex("v2");
        graph.addVertex("v3");
        graph.setEdgeWeight(graph.addEdge("v1", "v2"), 2);
        graph.setEdgeWeight(graph.addEdge("v2", "v3"), 2);
        graph.setEdgeWeight(graph.addEdge("v1", "v3"), 100);

        List<GraphPath> paths = new KShortestPaths(graph, 100).getPaths(source, target);

        assertThat(paths).hasSize(2);
        paths.forEach(it -> {
                    assertThat(it.getVertexList()).startsWith(source);
                    assertThat(it.getVertexList()).endsWith(target);
        });
    }

    @DisplayName("노선 한개짜리 그래프를 그리고 탐색하기")
    @Test
    void justOneLineTest() {
        String station1 = "강남역";
        String station2 = "역삼역";
        String station3 = "선릉역";

        WeightedMultigraph<String, DefaultWeightedEdge> graph = new WeightedMultigraph(DefaultWeightedEdge.class);
        graph.addVertex(station1);
        graph.addVertex(station2);
        graph.addVertex(station3);
        graph.setEdgeWeight(graph.addEdge(station1, station2), 2);
        graph.setEdgeWeight(graph.addEdge(station2, station3), 2);

        DijkstraShortestPath dijkstraShortestPath = new DijkstraShortestPath(graph);

        // 최단 경로 탐색 가능
        List<String> shortestPath = dijkstraShortestPath.getPath(station1, station3).getVertexList();
        assertThat(shortestPath.get(0)).isEqualTo(station1);
        assertThat(shortestPath.get(2)).isEqualTo(station3);

        // 탐색까지의 총 소요되는 가중치 계산 가능
        double pathWeight = dijkstraShortestPath.getPathWeight(station1, station3);
        assertThat(pathWeight).isEqualTo(4);

        // 경로의 그래프 확인 가능
        Arrays.asList(station1, station2, station3).forEach(station -> {
            ShortestPathAlgorithm.SingleSourcePaths paths = dijkstraShortestPath.getPaths(station);
            assertThat(paths.getGraph()).isEqualTo(graph);
        });
    }
}
