package study.jgraph;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.path.domain.SectionEdge;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.alg.shortestpath.KShortestPaths;
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
        graph.setEdgeWeight(graph.addEdge("v1", "v2"), 2);
        graph.setEdgeWeight(graph.addEdge("v2", "v3"), 2);
        graph.setEdgeWeight(graph.addEdge("v1", "v3"), 5);

        DijkstraShortestPath dijkstraShortestPath = new DijkstraShortestPath(graph);
        List<String> shortestPath = dijkstraShortestPath.getPath(source, target).getVertexList();

        assertThat(shortestPath.size()).isEqualTo(3);
    }

    @Test
    public void getDijkstraShortestPathWithStation() {
        Station source = new Station("1번");
        Station target = new Station("3번");
        WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph(DefaultWeightedEdge.class);
        graph.addVertex(new Station("1번"));
        graph.addVertex(new Station("2번"));
        graph.addVertex(new Station("3번"));
        graph.setEdgeWeight(graph.addEdge(new Station("1번"), new Station("2번")), 2);
        graph.setEdgeWeight(graph.addEdge(new Station("1번"), new Station("2번")), 2);
        graph.setEdgeWeight(graph.addEdge(new Station("2번"), new Station("3번")), 2);
        graph.setEdgeWeight(graph.addEdge(new Station("1번"), new Station("3번")), 5);

        DijkstraShortestPath dijkstraShortestPath = new DijkstraShortestPath(graph);
        List<String> shortestPath = dijkstraShortestPath.getPath(source, target).getVertexList();

        assertThat(shortestPath).isEqualTo(Arrays.asList(new Station("1번"), new Station("2번"), new Station("3번")));
        assertThat(dijkstraShortestPath.getPath(source, target).getWeight()).isEqualTo(4);
        assertThat(shortestPath.size()).isEqualTo(3);
    }

    @Test
    public void getDijkstraShortestPathWithSectionAndStation() {
        Line line1 = new Line("1번노선", "검정");
        Line line2 = new Line("2번노선", "검정");
        Line line3 = new Line("3번노선", "검정");

        Station station1 = new Station("1번");
        Station station2 = new Station("2번");
        Station station3 = new Station("3번");

        Section 섹션_1번_2번 = new Section(line1, station1, station2, 2);
        SectionEdge 섹션엣지_1번_2번 = SectionEdge.of(섹션_1번_2번);

        Section 섹션_2번_3번 = new Section(line2, station2, station3, 2);
        SectionEdge 섹션엣지_2번_3번 = SectionEdge.of(섹션_2번_3번);

        Section 섹션_1번_3번 = new Section(line3, station1, station3, 5);
        SectionEdge 섹션엣지_1번_3번 = SectionEdge.of(섹션_1번_3번);

        WeightedMultigraph<Station, SectionEdge> graph = new WeightedMultigraph(SectionEdge.class);
        graph.addVertex(station1);
        graph.addVertex(station2);
        graph.addVertex(station3);

        graph.addEdge(섹션_1번_2번.getUpStation(), 섹션_1번_2번.getDownStation(), 섹션엣지_1번_2번);
        graph.setEdgeWeight(섹션엣지_1번_2번, 섹션_1번_2번.getDistance());

        graph.addEdge(섹션_2번_3번.getUpStation(), 섹션_2번_3번.getDownStation(), 섹션엣지_2번_3번);
        graph.setEdgeWeight(섹션엣지_2번_3번, 섹션_2번_3번.getDistance());

        graph.addEdge(섹션_1번_3번.getUpStation(), 섹션_1번_3번.getDownStation(), 섹션엣지_1번_3번);
        graph.setEdgeWeight(섹션엣지_1번_3번, 섹션_1번_3번.getDistance());

        DijkstraShortestPath dijkstraShortestPath = new DijkstraShortestPath(graph);
        List<String> shortestPath = dijkstraShortestPath.getPath(station1, station3).getVertexList();

        assertThat(shortestPath).isEqualTo(Arrays.asList(new Station("1번"), new Station("2번"), new Station("3번")));
        assertThat(dijkstraShortestPath.getPath(station1, station3).getWeight()).isEqualTo(4);
        assertThat(dijkstraShortestPath.getPath(station1, station3).getEdgeList()).isEqualTo((Arrays.asList(섹션엣지_1번_2번, 섹션엣지_2번_3번)));

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
}
