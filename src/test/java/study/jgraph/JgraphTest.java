package study.jgraph;

import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.alg.shortestpath.KShortestPaths;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class JgraphTest {
    private static final String URI_GOOGLE = "http://www.google.com";
    private static final String URI_WIKIPEDIA = "http://www.wikipedia.org";
    private static final String URI_JGRAPHT = "http://www.jgrapht.org";
    private Graph<String, DefaultEdge> directedGraph = new DefaultDirectedGraph<>(DefaultEdge.class);

    @BeforeEach
    void setUp() {
        directedGraph.addVertex("a");
        directedGraph.addVertex("b");
        directedGraph.addVertex("c");
        directedGraph.addVertex("d");
        directedGraph.addVertex("e");
        directedGraph.addVertex("f");
        directedGraph.addVertex("g");
        directedGraph.addVertex("h");
        directedGraph.addVertex("i");

        /***
         *  a → b   f → g
         *  ↑   ↓   ↑ ↗
         *  c ← d ← e ← h ← i
         */
        directedGraph.addEdge("a", "b");
        directedGraph.addEdge("b", "d");
        directedGraph.addEdge("d", "c");
        directedGraph.addEdge("c", "a");
        directedGraph.addEdge("e", "d");
        directedGraph.addEdge("e", "f");
        directedGraph.addEdge("f", "g");
        directedGraph.addEdge("e", "g");
        directedGraph.addEdge("h", "e");
        directedGraph.addEdge("i", "h");
    }

    @Test
    @DisplayName("가중치를 이용한 다익스트라 알고리즘 Jgrapht 예제1")
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
    @DisplayName("가중치를 이용한 KShortest 알고리즘 Jgrapht 예제2")
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
    @DisplayName("방향 그래프를 만든다.")
    public void helloJGrapht() throws Exception {
        Graph<URI, DefaultEdge> g = new DefaultDirectedGraph<>(DefaultEdge.class);
        URI google = new URI(URI_GOOGLE);
        URI wikipedia = new URI(URI_WIKIPEDIA);
        URI jgrapht = new URI(URI_JGRAPHT);

        // add the vertices(정점)
        g.addVertex(google);
        g.addVertex(wikipedia);
        g.addVertex(jgrapht);

        /***
         *    wikipedia  ← jgrapht
         *       ↕     ↗
         *     google
         */
        // add edges(간선) to create linking structure
        g.addEdge(jgrapht, wikipedia);
        g.addEdge(google, jgrapht);
        g.addEdge(google, wikipedia);
        g.addEdge(wikipedia, google);

        //Graph Accessors 그래프 탐색하기
        URI start = g.vertexSet().stream()
                .filter(uri -> uri.getHost().equals(URI_JGRAPHT.split("//")[1]))
                .findAny()
                .get();

        assertThat(start).isEqualTo(jgrapht);
    }

    @Test
    @DisplayName("다익스트라 알고리즘 최단거리 구하기")
    public void dijkstraShortestPath() {
        //데이크스트라 알고리즘, 다익스트라 알고리즘.. 그래프 꼭지점 간 최단 경로를 찾는 알고리즘
        DijkstraShortestPath<String, DefaultEdge> dijkstraShortestPath = new DijkstraShortestPath<>(directedGraph);
        List<String> shortestPath = dijkstraShortestPath.getPath("i", "g").getVertexList();

        assertThat(shortestPath).hasSize(4);
    }

    @Test
    @DisplayName("KShort 알고리즘 최단거리 구하기")
    public void JGraphTAlgorithms() {
        List<GraphPath> paths = new KShortestPaths(directedGraph, 100).getPaths("i", "g");

        assertThat(paths).hasSize(2);
        paths.stream()
                .forEach(it -> {
                    assertThat(it.getVertexList()).startsWith("i");
                    assertThat(it.getVertexList()).endsWith("g");
                });
    }

}
