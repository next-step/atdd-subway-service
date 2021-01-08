package nextstep.subway.path.application;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PathServiceTest {

    private Station gangnam;
    private Station yangjae;
    private Station gyodae;
    private Station hongdae;
    private Line line;
    private Line line2;
    private List<Station> stations = new ArrayList<>();
    private List<Line> lines = new ArrayList<>();

    @Mock
    private StationRepository stationRepository;
    @Mock
    private LineRepository lineRepository;

    @BeforeEach
    public void setUp() {
        gangnam = new Station(1L, "강남역");
        yangjae = new Station(2L, "양재역");
        gyodae = new Station(3L, "교대역");
        hongdae = new Station(4L, "홍대역");
        line = new Line(1L,"2호선", "green", gangnam, yangjae, 10);
        line2 = new Line(2L,"3호선", "orange", yangjae, hongdae, 10);
        line.addSection(gangnam, gyodae, 5);
        stations.add(gangnam);
        stations.add(yangjae);
        stations.add(gyodae);
        stations.add(hongdae);
        lines.add(line);
        lines.add(line2);
    }

    @Test
    void findPath() {
        PathService pathService = new PathService(stationRepository, lineRepository);

        when(lineRepository.findAll()).thenReturn(lines);
        when(stationRepository.findById(gangnam.getId())).thenReturn(java.util.Optional.ofNullable(gangnam));
        when(stationRepository.findById(hongdae.getId())).thenReturn(java.util.Optional.ofNullable(hongdae));

        PathResponse pathResponse = pathService.findPathByIds(gangnam.getId(), hongdae.getId());

        assertThat(pathResponse).isNotNull();
        assertThat(pathResponse.getDistance()).isNotNull();
        assertThat(pathResponse.getDistance()).isEqualTo(20L);
    }

    @Test
    public void getDijkstraShortestPath() {
        WeightedMultigraph<String, DefaultWeightedEdge> graph
                = new WeightedMultigraph(DefaultWeightedEdge.class);
        graph.addVertex("v1");
        graph.addVertex("v2");
        graph.addVertex("v3");
        graph.addVertex("v4");
        graph.setEdgeWeight(graph.addEdge("v1", "v2"), 2);
        graph.setEdgeWeight(graph.addEdge("v2", "v3"), 2);
        graph.setEdgeWeight(graph.addEdge("v1", "v3"), 100);

        DijkstraShortestPath dijkstraShortestPath
                = new DijkstraShortestPath(graph);
        List<String> shortestPath
                = dijkstraShortestPath.getPath("v3", "v1").getVertexList();
        List<DefaultWeightedEdge> edgeList
                = dijkstraShortestPath.getPath("v3", "v1").getEdgeList();
        double weight
                = dijkstraShortestPath.getPath("v3", "v1").getWeight();

        shortestPath.forEach(vertex -> System.out.println(vertex));
        System.out.println(weight);
        assertThat(shortestPath.size()).isEqualTo(3);
    }

}
