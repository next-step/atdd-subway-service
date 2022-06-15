package nextstep.subway.path.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import java.util.Arrays;
import java.util.List;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.path.domain.Path;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("최단 경로 조회 기능")
@ExtendWith(MockitoExtension.class)
class PathServiceTest {
    @Mock
    private StationService stationService;
    @Mock
    private LineRepository lineRepository;
    @InjectMocks
    private PathService pathService;

    private Line 신분당선;
    private Line 이호선;
    private Line 삼호선;
    private Station 강남역;
    private Station 양재역;
    private Station 교대역;
    private Station 남부터미널역;

    /**
     * 교대역    --- *2호선* ---   강남역
     * |                        |
     * *3호선*                   *신분당선*
     * |                        |
     * 남부터미널역  --- *3호선* --- 양재역
     */
    @BeforeEach
    public void setUp() {
        강남역 = new Station("강남역");
        양재역 = new Station("양재역");
        교대역 = new Station("교대역");
        남부터미널역 = new Station("남부터미널역");

        신분당선 = new Line("신분당선", "red", 강남역, 양재역, 10);
        이호선 = new Line("이호선", "green", 교대역, 강남역, 10);
        삼호선 = new Line("삼호선", "orange", 교대역, 양재역, 5);
        삼호선.addSection(교대역, 남부터미널역, 3);
    }

    @DisplayName("교대역에서 양재역까지 최단 경로 조회를 요청하면, 최단 경로가 조회된다.")
    @Test
    void findShortestPath() {
        //given
        Long 교대역_ID = 3L;
        Long 양재역_ID = 2L;
        given(lineRepository.findAll()).willReturn(Arrays.asList(신분당선, 이호선, 삼호선));
        given(stationService.findStationById(교대역_ID)).willReturn(교대역);
        given(stationService.findStationById(양재역_ID)).willReturn(양재역);

        //when
        Path path = 최단_경로_조회함(교대역_ID, 양재역_ID);

        //then
        경유지_확인(path, Arrays.asList(교대역, 남부터미널역, 양재역));
        경유거리_확인(path, 5);
    }

    private Path 최단_경로_조회함(Long sourceStationId, Long targetStationId) {
        return pathService.findShortestPath(sourceStationId, targetStationId);
    }

    private void 경유거리_확인(Path path, int expectedDistance) {
        assertThat(path.getDistance()).isEqualTo(expectedDistance);
    }

    private void 경유지_확인(Path path, List<Station> expectedStations) {
        List<Station> actualStations = path.getStations();
        assertThat(actualStations).isEqualTo(expectedStations);
        assertThat(actualStations).hasSize(3);
    }
}
