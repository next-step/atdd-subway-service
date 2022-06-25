package nextstep.subway.path.application;

import nextstep.subway.exception.IllegalArgumentException;
import nextstep.subway.exception.NoSuchElementFoundException;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class PathServiceTest {
    @Mock
    private LineRepository lineRepository;

    @Mock
    private StationService stationService;

    private Line 신분당선;
    private Line 이호선;
    private Line 삼호선;

    private Station 강남역;
    private Station 양재역;
    private Station 교대역;
    private Station 남부터미널역;

    /**
     * 교대역    --- *2호선* (7) ---   강남역
     * |                              |
     * *3호선*                      *신분당선*
     * (3)                           (10)
     * |                              |
     * 남부터미널역  --- *3호선* (2)---   양재
     */
    @BeforeEach
    public void setUp() {
        강남역 = new Station("강남역");
        양재역 = new Station("양재역");
        교대역 = new Station("교대역");
        남부터미널역 = new Station("남부터미널역");

        신분당선 = new Line("신분당선", "bg-red-600", 강남역, 양재역, 10);
        이호선 = new Line("이호선", "bg-green-600", 교대역, 강남역, 7);
        삼호선 = new Line("삼호선", "bg-orange-600", 교대역, 양재역, 5);

        삼호선.addSection(교대역, 남부터미널역, 3);
    }

    @DisplayName("양재역과 교대역의 최단 경로를 조회한다.")
    @Test
    void 최단_경로_조회() {
        //given
        PathService pathService = new PathService(lineRepository, stationService);
        when(lineRepository.findAll()).thenReturn(Arrays.asList(신분당선, 이호선, 삼호선));
        when(stationService.findStationById(2L)).thenReturn(양재역);
        when(stationService.findStationById(3L)).thenReturn(교대역);

        //when
        PathResponse pathResponse = pathService.findShortestPath(2L, 3L);

        //then
        assertThat(pathResponse).isNotNull();
        assertThat(pathResponse.getDistance()).isEqualTo(5);

    }

    @Test
    void 출발역과_도착역이_같은_경우_에러() {
        //given
        PathService pathService = new PathService(lineRepository, stationService);
        when(lineRepository.findAll()).thenReturn(Arrays.asList(신분당선, 이호선, 삼호선));
        when(stationService.findStationById(2L)).thenReturn(양재역);

        assertThrows(IllegalArgumentException.class, () -> pathService.findShortestPath(2L, 2L));
    }

    @Test
    void 노선에_등록되지_않은_역_최단_경로_조회시_에러_발생() {
        //given
        PathService pathService = new PathService(lineRepository, stationService);
        when(lineRepository.findAll()).thenReturn(Arrays.asList(신분당선, 이호선, 삼호선));
        when(stationService.findStationById(2L)).thenReturn(양재역);

        assertThrows(NoSuchElementFoundException.class,() -> pathService.findShortestPath(2L, 9L));
    }
}
