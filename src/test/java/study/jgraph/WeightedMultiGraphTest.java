package study.jgraph;

import org.assertj.core.api.Assertions;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.UnmodifiableUndirectedGraph;
import org.jgrapht.graph.WeightedMultigraph;
import org.jgrapht.graph.builder.UndirectedWeightedGraphBuilderBase;
import org.junit.jupiter.api.Test;

import java.util.List;

class WeightedMultiGraphTest {

    @Test
    void getDijkstraShortestPath() {
        // given
        final UndirectedWeightedGraphBuilderBase<String, DefaultWeightedEdge, ? extends WeightedMultigraph<String, DefaultWeightedEdge>, ?> builder = WeightedMultigraph.builder(DefaultWeightedEdge.class);
        final UnmodifiableUndirectedGraph<String, DefaultWeightedEdge> graph = builder
                .addVertex("v1")
                .addVertex("v2")
                .addVertex("v3")
                .addEdge("v1", "v2", 2)
                .addEdge("v2", "v3", 2)
                .addEdge("v1", "v3", 100)
                .buildUnmodifiable();
        final String source = "v3";
        final String target = "v1";
        // when
        final List<String> shortestPath = DijkstraShortestPath.findPathBetween(graph, source, target).getVertexList();
        // then
        Assertions.assertThat(shortestPath.size()).isEqualTo(3);
    }
}
