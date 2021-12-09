package nextstep.graph;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;

public class GraphTest {

    @Test
    void shortestPathTest() {
        String source = "v3";
        String target = "v1";

        List<String> vertexes = Lists.newArrayList("v1", "v2", "v3");
        List<GraphEdge<String>> edges = Lists.newArrayList(
            new GraphEdge<>("v1", "v2", 2),
            new GraphEdge<>("v2", "v3", 2),
            new GraphEdge<>("v1", "v3", 100)
        );

        Graph<String> graph = new Graph<>(vertexes, edges);
        List<String> shortestPath = graph.getShortestPathList(source, target);

        assertThat(shortestPath.size()).isEqualTo(3);
    }

}
