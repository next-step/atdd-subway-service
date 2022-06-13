package study.jgraph;

import java.util.stream.Collectors;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.alg.shortestpath.KShortestPaths;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

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
        paths.stream()
                .forEach(it -> {
                    assertThat(it.getVertexList()).startsWith(source);
                    assertThat(it.getVertexList()).endsWith(target);
                });
    }

    @Test
    @DisplayName("역과 역사이 최단거리를 구한다.")
    void getDijkstraShortestPathWithStation() {
        Station 공덕역 = new Station("공덕역");
        Station 여의도역 = new Station("여의도역");
        Station 합정역 = new Station("합정역");
        Station 당산역 = new Station("당산역");

        WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
        graph.addVertex(공덕역);
        graph.addVertex(여의도역);
        graph.addVertex(합정역);
        graph.addVertex(당산역);
        graph.setEdgeWeight(graph.addEdge(공덕역, 합정역), 4);
        graph.setEdgeWeight(graph.addEdge(합정역, 당산역), 1);
        graph.setEdgeWeight(graph.addEdge(공덕역, 여의도역), 4);
        graph.setEdgeWeight(graph.addEdge(여의도역, 당산역), 2);

        DijkstraShortestPath<Station, DefaultWeightedEdge> dijkstraShortestPath = new DijkstraShortestPath<>(graph);
        GraphPath<Station, DefaultWeightedEdge> path = dijkstraShortestPath.getPath(공덕역, 당산역);
        List<Station> paths = path.getVertexList();
        List<String> names = paths.stream()
                .map(Station::getName)
                .collect(Collectors.toList());

        assertThat(paths.size()).isEqualTo(3);
        assertThat(names).containsExactly("공덕역", "합정역", "당산역");
        assertThat(path.getWeight()).isEqualTo(5);
    }
}
