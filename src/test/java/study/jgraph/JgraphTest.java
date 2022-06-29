package study.jgraph;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.alg.shortestpath.KShortestPaths;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.junit.jupiter.api.Test;

public class JgraphTest {

    /**
     * 'v1' - (100) - `v3`
     *  |             |
     *   (2)       (2)
     *     |      |
     *        v2
     *
     * path : v3 -> v1 = 102
     * path : v3 -> v2 -> v1 = 4
     */
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

        assertThat(shortestPath)
            .hasSize(3)
            .containsExactly("v3", "v2", "v1");
    }

    /**
     * `v1` - (2) -  v2
     * |
     * (100)
     * |
     * v3 - (10) - 'v4'
     *
     * path : v1 -> v3, v3 -> v4 = 110
     */
    @Test
    public void getKShortestPaths() {
        String source = "v1";
        String target = "v4";

        WeightedMultigraph<String, DefaultWeightedEdge> graph = new WeightedMultigraph(DefaultWeightedEdge.class);
        graph.addVertex("v1");
        graph.addVertex("v2");
        graph.addVertex("v3");
        graph.addVertex("v4");
        graph.setEdgeWeight(graph.addEdge("v1", "v2"), 2);
        graph.setEdgeWeight(graph.addEdge("v1", "v3"), 100);
        graph.setEdgeWeight(graph.addEdge("v3", "v4"), 10);

        List<GraphPath> paths = new KShortestPaths(graph, 100).getPaths(source, target);
        assertThat(paths)
            .hasSize(1)
            .allSatisfy(it -> {
                assertThat(it.getVertexList()).startsWith(source);
                assertThat(it.getVertexList()).endsWith(target);
            });
    }
}
