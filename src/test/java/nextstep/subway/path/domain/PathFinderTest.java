package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.station.domain.Station;
import org.assertj.core.util.Arrays;
import org.assertj.core.util.Lists;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;

@DisplayName("단위 테스트 - mockito의 MockitoExtension을 활용한 가짜 협력 객체 사용")
@ExtendWith(MockitoExtension.class)
class PathFinderTest {
    @Mock
    private LineRepository lineRepository;

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
        graph.setEdgeWeight(graph.addEdge(교대역.toString(), 강남역.toString()), 10);
        graph.setEdgeWeight(graph.addEdge(강남역.toString(), 양재역.toString()), 10);
        graph.setEdgeWeight(graph.addEdge(교대역.toString(), 양재역.toString()), 5);

        DijkstraShortestPath dijkstraShortestPath = new DijkstraShortestPath(graph);

        List<Station> shortestPath
                = dijkstraShortestPath.getPath(교대역.toString(), 양재역.toString()).getVertexList();

        assertThat(shortestPath.size()).isEqualTo(2);

        double shortDistance
                = dijkstraShortestPath.getPath(교대역.toString(), 양재역.toString()).getWeight();
        assertThat(Integer.parseInt(String.valueOf(Math.round(shortDistance)))).isEqualTo(5);
    }

    @Test
    @DisplayName("경로 조회 메소드 테스트")
    void findRouteSearch() {
        when(lineRepository.findAll())
                .thenReturn(Lists.newArrayList(
                        new Line("삼호선", "orange darken-1", new Station("교대역"), new Station("남부터미널"), 3),
                        new Line("삼호선", "orange darken-1", new Station("남부터미널"), new Station("남부터미널"), 2),
                        new Line("이호선", "green lighten-1", new Station("교대역"), new Station("강남역"), 10),
                        new Line("신분당선", "red lighten-1", new Station("강남역"), new Station("양재역"), 10)
                ));

        PathFinder path = new PathFinder();
        path.findRouteSearch(교대역, 양재역);

        assertThat(path.getStation().size()).isEqualTo(0);
    }
}