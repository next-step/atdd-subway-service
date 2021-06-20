package study.jgraph;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.alg.shortestpath.KShortestPaths;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class JgraphTest {

    @Test
    void kShortest_를_이용한_최단거리역_적용_테스트() {
        Station source = Station.of("출발역");
        Station mid1 = Station.of("중간역");
        Station target = Station.of("도착역");

        Line line1 = new Line("1호선", "파란색", source, target, 100);
        Section section1 = new Section(line1, source, mid1, 50);
        line1.addSection(section1);

        WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph(DefaultWeightedEdge.class);

        List<Section> sections = line1.getSections().get();
        for (Section section : sections) {
            graph.addVertex(section.getUpStation());
            graph.addVertex(section.getDownStation());
            graph.setEdgeWeight(graph.addEdge(section.getUpStation(), section.getDownStation()), section.getDistance());
        }

        List<GraphPath> paths = new KShortestPaths(graph, 100).getPaths(source, target);

        System.out.println("getKShortestPaths");
        for (GraphPath path : paths) {
            List<Station> vertexList = path.getVertexList();
            for (Station station : vertexList) {
                System.out.println(station.getName());
            }
        }
        System.out.println("");

        assertThat(paths).hasSize(1);
        paths.stream()
                .forEach(it -> {
                    assertThat(it.getVertexList()).startsWith(source);
                    assertThat(it.getVertexList()).endsWith(target);
                });
    }

    @Test
    void dijkstraShortest_를_이용한_최단거리역_적용_테스트() {
        Station source = Station.of("출발역");
        Station mid1 = Station.of("중간역");
        Station target = Station.of("도착역");

        Line line1 = new Line("1호선", "파란색", source, target, 100);
        Section section1 = new Section(line1, source, mid1, 50);
        line1.addSection(section1);

        WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph(DefaultWeightedEdge.class);

        List<Section> sections = line1.getSections().get();
        for (Section section : sections) {
            graph.addVertex(section.getUpStation());
            graph.addVertex(section.getDownStation());
            graph.setEdgeWeight(graph.addEdge(section.getUpStation(), section.getDownStation()), section.getDistance());
        }

        DijkstraShortestPath dijkstraShortestPath = new DijkstraShortestPath(graph);
        List<Station> shortestPath = dijkstraShortestPath.getPath(source, target).getVertexList();

        System.out.println("getDijkstraShortestPath");
        for (Station station : shortestPath) {
            System.out.println(station.getName());
        }
        System.out.println("");

        assertThat(shortestPath.size()).isEqualTo(3);
    }

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

        System.out.println("getDijkstraShortestPath");
        System.out.println(shortestPath);

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

        System.out.println("getKShortestPaths");
        System.out.println(paths);

        assertThat(paths).hasSize(2);
        paths.stream()
                .forEach(it -> {
                    assertThat(it.getVertexList()).startsWith(source);
                    assertThat(it.getVertexList()).endsWith(target);
                });
    }
}
