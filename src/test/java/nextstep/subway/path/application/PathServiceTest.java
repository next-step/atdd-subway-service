package nextstep.subway.path.application;

import nextstep.subway.line.application.LineService;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.domain.SectionRepository;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@DataJpaTest
@ExtendWith(MockitoExtension.class)
class PathServiceTest {

    @Autowired
    private StationRepository stationRepository;

    @Autowired
    private SectionRepository sectionRepository;

    @Autowired
    private LineRepository lineRepository;

    @Mock
    private StationService stationService;

    @Mock
    private LineService lineService;

    private PathService pathService;

    private Station 교대역;
    private Station 남부터미널역;
    private Station 강남역;
    private Station 양재역;

    private Line 신분당선;
    private Line 이호선;
    private Line 삼호선;

    /**
     * (10)
     * 교대역    --- *2호선* ---       강남역
     * |                                |
     * *3호선*(3)                    *신분당선*(10)
     * |                               |
     * 남부터미널역  --- *3호선*(2) ---   양재
     */

    @BeforeEach
    void setUp() {
        pathService = new PathService(stationService, lineService);
        createStation();
        createLine();
    }

    @Test
    @DisplayName("시작역과 도착역의 최단거리를 구하는 기능 테스트 : 교대역에서 양재역까지 최단거리")
    void getDijkstraSortestPath() {
        // given
        Station 시작점 = 교대역;
        Station 도착점 = 양재역;
        when(stationService.findAll()).thenReturn(stationRepository.findAll());
        when(lineService.findAllSection()).thenReturn(sectionRepository.findAll());

        WeightedMultigraph<Station, DefaultWeightedEdge> graph = pathService.createGraph();
        // when
        DijkstraShortestPath dijkstraShortestPath = new DijkstraShortestPath(graph);
        List<Station> shortestPath = dijkstraShortestPath.getPath(시작점, 도착점).getVertexList();
        double pathWeight = dijkstraShortestPath.getPathWeight(시작점, 도착점);

        // then
        assertThat(shortestPath.size()).isEqualTo(3);
        assertThat(pathWeight).isEqualTo(5.0);
    }


    private void createStation() {
        교대역 = new Station("교대역");
        남부터미널역 = new Station("남부터미널역");
        강남역 = new Station("강남역");
        양재역 = new Station("양재역");

        stationRepository.saveAll(Arrays.asList(교대역, 남부터미널역, 강남역, 양재역));

    }

    private void createLine() {
        신분당선 = new Line("신분당선", "bg-red-600", 강남역, 양재역, 10);
        이호선 = new Line("이호선", "green", 교대역, 강남역, 10);
        삼호선 = new Line("삼호선", "orange", 교대역, 양재역, 5);

        lineRepository.saveAll(Arrays.asList(신분당선, 이호선, 삼호선));
        addSection();
    }

    private void addSection() {
        삼호선.addSection(교대역, 남부터미널역, 3);
    }
}