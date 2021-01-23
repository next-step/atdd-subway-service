package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Line;
import nextstep.subway.station.domain.Station;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class PathFinderTest {

    private Line 신분당선;
    private Line 이호선;
    private Line 삼호선;
    private Station 강남역;
    private Station 양재역;
    private Station 교대역;
    private Station 남부터미널역;

    @BeforeEach
    public void setUp() {

        강남역 = new Station("강남역");
        양재역 = new Station("양재역");
        교대역 = new Station("교대역");
        남부터미널역 = new Station("남부터미널");
        신분당선 = new Line("신분당선", "red lighten-1", 강남역, 양재역, 10);
        이호선 = new Line("2호선", "green lighten-1", 교대역, 강남역, 10);
        삼호선 = new Line("3호선", "orange darken-1", 교대역, 양재역, 5);


        삼호선.addLineSection(삼호선, 교대역, 남부터미널역, 3);
    }

    @Test
    @DisplayName("최단경로 구하기")
    void getDijkstraShortestPath() {
        WeightedMultigraph<String, DefaultWeightedEdge> graph
                = new WeightedMultigraph(DefaultWeightedEdge.class);
        graph.addVertex(교대역.toString());
        graph.addVertex(강남역.toString());
        graph.addVertex(양재역.toString());
        graph.addVertex(남부터미널역.toString());
        graph.setEdgeWeight(graph.addEdge(교대역.toString(), 강남역.toString()), 10);
        graph.setEdgeWeight(graph.addEdge(강남역.toString(), 양재역.toString()), 10);
        graph.setEdgeWeight(graph.addEdge(교대역.toString(), 남부터미널역.toString()), 3);
        graph.setEdgeWeight(graph.addEdge(남부터미널역.toString(), 양재역.toString()), 2);

        DijkstraShortestPath dijkstraShortestPath = new DijkstraShortestPath(graph);

        List<Station> shortestPath
                = dijkstraShortestPath.getPath(교대역.toString(), 양재역.toString()).getVertexList();

        assertThat(shortestPath.size()).isEqualTo(3);

        double shortDistance
                = dijkstraShortestPath.getPath(교대역.toString(), 양재역.toString()).getWeight();
        assertThat(Integer.parseInt(String.valueOf(Math.round(shortDistance)))).isEqualTo(5);
    }

    @Test
    @DisplayName("최단경로 id값으로 구하기")
    void getDijkstraShortestPathById() {
        ReflectionTestUtils.setField(강남역, "id", 1L);
        ReflectionTestUtils.setField(양재역, "id", 2L);
        ReflectionTestUtils.setField(교대역, "id", 3L);

        WeightedMultigraph<String, DefaultWeightedEdge> graph
                = new WeightedMultigraph(DefaultWeightedEdge.class);
        
        String 교대 = String.valueOf(교대역.getId());
        String 강남 = String.valueOf(강남역.getId());
        String 양재 = String.valueOf(양재역.getId());


        graph.addVertex(교대);
        graph.addVertex(강남);
        graph.addVertex(양재);
        graph.setEdgeWeight(graph.addEdge(교대, 강남), 10);
        graph.setEdgeWeight(graph.addEdge(강남, 양재), 10);
        graph.setEdgeWeight(graph.addEdge(교대, 양재), 5);

        DijkstraShortestPath dijkstraShortestPath = new DijkstraShortestPath(graph);

        List<Station> shortestPath
                = dijkstraShortestPath.getPath(교대, 양재).getVertexList();

        assertThat(shortestPath.size()).isEqualTo(2);

        double shortDistance
                = dijkstraShortestPath.getPath(교대, 양재).getWeight();
        assertThat(Integer.parseInt(String.valueOf(Math.round(shortDistance)))).isEqualTo(5);
    }    

    @Test
    @DisplayName("경로 조회 메소드 테스트")
    void findRouteSearch() {
        ReflectionTestUtils.setField(강남역, "id", 1L);
        ReflectionTestUtils.setField(양재역, "id", 2L);
        ReflectionTestUtils.setField(교대역, "id", 3L);
        ReflectionTestUtils.setField(남부터미널역, "id", 4L);
        ReflectionTestUtils.setField(신분당선, "id", 1L);
        ReflectionTestUtils.setField(이호선, "id", 2L);
        ReflectionTestUtils.setField(삼호선, "id", 3L);

        List<Line> lines = new ArrayList<>();
        lines.add(이호선);
        lines.add(삼호선);
        lines.add(신분당선);
        PathFinder path = new PathFinder();
        path.findRouteSearch(교대역, 양재역, lines);

        assertThat(path.getStation().size()).isEqualTo(3);
        assertThat(path.getDistance()).isEqualTo(5);
    }
}