package nextstep.subway.path.application;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.domain.Section;
import nextstep.subway.path.domain.SubwayGraph;
import nextstep.subway.path.domain.SubwayPath;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MapServiceTest {
    public static final long 교대역_id = 2L;
    public static final long 양재역_id = 3L;

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

    @DisplayName("최단 경로와 최단 거리와 운임 요금 구하기")
    @Test
    void findShortestPath() {
        //given
        when(lineRepository.findAll()).thenReturn(Arrays.asList(신분당선, 이호선, 삼호선));
        when(stationRepository.findById(교대역_id)).thenReturn(Optional.of(교대역));
        when(stationRepository.findById(양재역_id)).thenReturn(Optional.of(양재역));
        when(pathService.findPath(Arrays.asList(신분당선, 이호선, 삼호선), 교대역, 양재역))
                .thenReturn(new SubwayPath(Arrays.asList(교대역, 남부터미널역, 양재역),
                        Arrays.asList(new Section(삼호선, 교대역, 남부터미널역, 3),
                                      new Section(삼호선, 남부터미널역, 양재역, 2)), 5));

        MapService mapService = new MapService(stationRepository, lineRepository, pathService);

        //when
        PathResponse shortestPath = mapService.findShortestPath(new LoginMember(1L, "a@email.com", 10), 교대역_id, 양재역_id);

        //then
        verify(lineRepository, times(1)).findAll();
        verify(stationRepository, times(1)).findById(교대역_id);
        verify(stationRepository, times(1)).findById(양재역_id);
        verify(pathService, times(1)).findPath(Arrays.asList(신분당선, 이호선, 삼호선), 교대역, 양재역);
        assertAll(
                () -> assertThat(shortestPath.getDistance()).isEqualTo(5),
                () -> assertThat(shortestPath.getStations().get(0).getName()).isEqualTo("교대역"),
                () -> assertThat(shortestPath.getStations().get(1).getName()).isEqualTo("남부터미널역"),
                () -> assertThat(shortestPath.getStations().get(2).getName()).isEqualTo("양재역"),
                () -> assertThat(shortestPath.getFare()).isEqualTo(450)
        );
    }
}
