package nextstep.subway.path.application;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.path.domain.SubwayGraph;
import nextstep.subway.path.domain.SubwayPath;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MapServiceTest {
    @Mock
    private StationRepository stationRepository;
    @Mock
    private LineRepository lineRepository;
    @Mock
    private PathService pathService;

    private Station 강남역;
    private Station 교대역;
    private Station 양재역;
    private Station 남부터미널역;
    private Line 신분당선;
    private Line 이호선;
    private Line 삼호선;

    @BeforeEach
    void setUp() {
        강남역 = new Station("강남역");
        교대역 = new Station("교대역");
        양재역 = new Station("양재역");
        남부터미널역 = new Station("남부터미널역");

        신분당선 = new Line("신분당선", "bg-red-600", 강남역, 양재역, 10);
        이호선 = new Line("이호선", "bg-red-600", 교대역, 강남역, 10);
        삼호선 = new Line("삼호선", "bg-red-600", 교대역, 양재역, 5);
        삼호선.addLineSection(교대역, 남부터미널역, 3);
    }

    @Test
    void findShortestPath() {
        //given
        when(lineRepository.findAll()).thenReturn(Arrays.asList(신분당선, 이호선, 삼호선));
        when(stationRepository.findById(2L)).thenReturn(Optional.of(교대역));
        when(stationRepository.findById(3L)).thenReturn(Optional.of(양재역));

        SubwayGraph subwayGraph = new SubwayGraph(DefaultWeightedEdge.class);
        subwayGraph.addVertexWithStations(Arrays.asList(신분당선, 이호선, 삼호선));
        subwayGraph.setEdgeWeightWithSections(Arrays.asList(신분당선, 이호선, 삼호선));
        DijkstraShortestPath dijkstraShortestPath = new DijkstraShortestPath(subwayGraph);
        GraphPath graphPath = dijkstraShortestPath.getPath(교대역, 양재역);
        when(pathService.findPath(Arrays.asList(신분당선, 이호선, 삼호선), 교대역, 양재역)).thenReturn(new SubwayPath(graphPath));

        MapService mapService = new MapService(stationRepository, lineRepository, pathService);

        //when
        PathResponse shortestPath = mapService.findShortestPath(2L, 3L);

        //then
        assertAll(
                () -> assertThat(shortestPath.getDistance()).isEqualTo(5),
                () -> assertThat(shortestPath.getStations().get(0).getName()).isEqualTo("교대역"),
                () -> assertThat(shortestPath.getStations().get(1).getName()).isEqualTo("남부터미널역"),
                () -> assertThat(shortestPath.getStations().get(2).getName()).isEqualTo("양재역")
        );
    }
}
