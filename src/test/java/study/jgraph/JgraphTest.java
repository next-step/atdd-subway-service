package study.jgraph;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;

import org.jgrapht.alg.shortestpath.YenKShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
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
    public void getDijkstraShortestPathWithLabelEdge() {
        String source = "v3";
        String target = "v1";
        WeightedMultigraph<String, CustomWeightEdge> graph = new WeightedMultigraph(CustomWeightEdge.class);
        graph.addVertex("v1");
        graph.addVertex("v2");
        graph.addVertex("v3");
        // DefaultWeightedEdge defaultWeightedEdge = graph.addEdge("v1", "v2");
        // graph.setEdgeWeight(, 2);
        CustomWeightEdge line1 = new CustomWeightEdge("line1");
        graph.addEdge("v2", "v3", line1);
        graph.setEdgeWeight(line1,2);

        CustomWeightEdge line2 = new CustomWeightEdge("line2");
        graph.addEdge("v1", "v3", line2);
        graph.setEdgeWeight(line2,100);

        CustomWeightEdge line3 = new CustomWeightEdge("line3");
        graph.addEdge("v1", "v2", line3);
        graph.setEdgeWeight(line3,2);

        DijkstraShortestPath dijkstraShortestPath = new DijkstraShortestPath(graph);



        GraphPath path = dijkstraShortestPath.getPath(source, target);
        assertThat(path.getWeight()).isEqualTo(4.0);
    }

    @Test
    public void getShortestPathWhenSourceAndTargetSame() {
        String source = "v3";
        String target = "v3";
        WeightedMultigraph<String, DefaultWeightedEdge> graph = new WeightedMultigraph(DefaultWeightedEdge.class);
        graph.addVertex("v1");
        graph.addVertex("v2");
        graph.addVertex("v3");
        graph.setEdgeWeight(graph.addEdge("v1", "v2"), 2);
        graph.setEdgeWeight(graph.addEdge("v2", "v3"), 2);
        graph.setEdgeWeight(graph.addEdge("v1", "v3"), 100);

        DijkstraShortestPath dijkstraShortestPath = new DijkstraShortestPath(graph);
        List<String> shortestPath = dijkstraShortestPath.getPath(source, target).getVertexList();
        System.out.println(shortestPath);
        assertThat(shortestPath.size()).isEqualTo(1);
    }

    @Test
    public void getNotOnlyOneBestPath() {
        String source = "v3";
        String target = "v1";
        WeightedMultigraph<String, DefaultWeightedEdge> graph = new WeightedMultigraph(DefaultWeightedEdge.class);
        graph.addVertex("v1");
        graph.addVertex("v2");
        graph.addVertex("v4");
        graph.addVertex("v3");

        graph.setEdgeWeight(graph.addEdge("v1", "v2"), 2);
        graph.setEdgeWeight(graph.addEdge("v1", "v4"), 2);
        graph.setEdgeWeight(graph.addEdge("v4", "v3"), 2);
        graph.setEdgeWeight(graph.addEdge("v2", "v3"), 2);


        DijkstraShortestPath dijkstraShortestPath = new DijkstraShortestPath(graph);
        List<String> shortestPath = dijkstraShortestPath.getPath(source, target).getVertexList();
        /*
         source를 기준으로 edge들을 iterate 하며 찾을때, addEdge된 순서대로 찾으므로, v4가 포함된 경로가 먼저 모두 add 되었기 때문에
         v3 -> v4 -> v1, v3 -> v2 -> v1 두 경로의 비용이 같아도 v3 -> v4 -> v1 경로로 결정됨
         */
        assertThat(shortestPath).containsExactlyElementsOf(Arrays.asList("v3","v4","v1"));
    }

    @Test
    public void notFoundPath() {
        String source = "v3";
        String target = "v4";
        WeightedMultigraph<String, DefaultWeightedEdge> graph = new WeightedMultigraph(DefaultWeightedEdge.class);
        graph.addVertex("v1");
        graph.addVertex("v2");
        graph.addVertex("v4");
        graph.addVertex("v3");

        graph.setEdgeWeight(graph.addEdge("v1", "v2"), 2);
        graph.setEdgeWeight(graph.addEdge("v2", "v3"), 2);


        DijkstraShortestPath dijkstraShortestPath = new DijkstraShortestPath(graph);
        GraphPath path = dijkstraShortestPath.getPath(source, target);
        // 경로를 찾지 못하면 null 리턴
        assertThat(path).isNull();
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

        // KShortestSimplePaths는 jgrapht-core 1.5.0에서 deprecated 됨. 대신 YenKShortestPath를 사용하도록 가이드 됨
        List<GraphPath> paths = new YenKShortestPath(graph).getPaths(source, target,100);

        assertThat(paths).hasSize(2);
        paths.stream()
                .forEach(it -> {
                    System.out.println(it.getVertexList());
                    assertThat(it.getVertexList()).startsWith(source);
                    assertThat(it.getVertexList()).endsWith(target);
                });
    }


}
