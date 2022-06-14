package nextstep.subway.path.application;

import nextstep.subway.line.application.LineService;
import nextstep.subway.line.domain.Line;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static nextstep.subway.line.domain.LineTest.노선_생성;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PathServiceTest {

    @Mock
    private StationService stationService;

    @Mock
    private LineService lineService;

    @InjectMocks
    private PathService pathService;

    private Station 교대역, 강남역, 역삼역, 선릉역, 한티역, 남부터미널역, 양재역, 매봉역, 도곡역, 오남역, 진접역;
    private Line 이호선, 삼호선, 사호선, 신분당선, 수인분당선;

    /**
     * 오남역--(4호선, 3)--진접역
     *
     * 교대역--(2호선, 3)--강남역--(2호선, 3)--역삼역--(2호선, 3)--선릉역
     *     |                   |                               (수인분당, 3)
     *  (3호선, 5)          (신분당, 3)                             한티역
     *     |                    |                               (수인분당, 3)
     *  남부터미널역--(3호선, 3)--양재역--(3호선, 3)--매봉역--(3호선, 3)--도곡역
     */

    @BeforeEach
    void setUp() {
        교대역 = mock(Station.class);
        강남역 = mock(Station.class);
        역삼역 = mock(Station.class);
        선릉역 = mock(Station.class);
        한티역 = mock(Station.class);
        남부터미널역 = mock(Station.class);
        양재역 = mock(Station.class);
        매봉역 = mock(Station.class);
        도곡역 = mock(Station.class);
        오남역 = mock(Station.class);
        진접역 = mock(Station.class);

        이호선 = 노선_생성("2호선", "bg-green-color", 교대역, 강남역, 3);
        이호선.addStation(강남역, 역삼역, 3);
        이호선.addStation(역삼역, 선릉역, 3);

        삼호선 = 노선_생성("3호선", "bg-orange-color", 교대역, 남부터미널역, 5);
        삼호선.addStation(남부터미널역, 양재역, 3);
        삼호선.addStation(양재역, 매봉역, 3);
        삼호선.addStation(매봉역, 도곡역, 3);

        사호선 = 노선_생성("4호선", "bg-sky-color", 오남역, 진접역, 3);

        신분당선 = 노선_생성("신분당선", "bg-red-color", 강남역, 양재역, 3);

        수인분당선 = 노선_생성("수인분당선", "bg-yellow-color", 선릉역, 한티역, 3);
        수인분당선.addStation(한티역, 도곡역, 3);
    }

    @DisplayName("노선 목록 중 하나의 노선에 속한 2개의 역의 최소 경로를 조회하면 정상 동작해야 한다")
    @Test
    void findShortestPathByOneLine() {
        // given
        stubServiceReturns();

        // when
        PathResponse.ShortestPath stations = pathService.findShortestPath(교대역.getId(), 선릉역.getId());

        // then
        최소_노선_경로_일치됨(stations, 교대역, 강남역, 역삼역, 선릉역);
        최소_노선_길이_일치됨(stations.getDistance(), 9);
    }

    @DisplayName("노선 목록 중 한번의 환승을 통해 도달할 수 있는 최소 경로를 조회하면 정상 동작해야 한다")
    @Test
    void findShortestPathByOneTransfer() {
        // given
        stubServiceReturns();

        // when
        PathResponse.ShortestPath stations = pathService.findShortestPath(교대역.getId(), 양재역.getId());

        // then
        최소_노선_경로_일치됨(stations, 교대역, 강남역, 양재역);
        최소_노선_길이_일치됨(stations.getDistance(), 6);
    }

    @DisplayName("노선 목록 중 도달할 수 있는 여러 경로를 가진 목적지의 경로를 조회하면 최소 경로로 조회해야 한다")
    @Test
    void findShortestPathByMultiplePath() {
        // given
        stubServiceReturns();

        // when
        PathResponse.ShortestPath stations = pathService.findShortestPath(교대역.getId(), 도곡역.getId());

        // then
        최소_노선_경로_일치됨(stations, 교대역, 강남역, 양재역, 매봉역, 도곡역);
        최소_노선_길이_일치됨(stations.getDistance(), 12);
    }

    @DisplayName("출발역과 도착역이 같은 노선을 조회하면 예외가 발생해야 한다")
    @Test
    void findShortestPathBySameStartAndEndStation() {
        // given
        stubServiceReturns();

        // then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> pathService.findShortestPath(교대역.getId(), 교대역.getId()));
    }

    @DisplayName("도달할 수 없는 경로를 조회하면 예외가 발생해야 한다")
    @Test
    void findShortestPathByUnreachablePath() {
        // given
        stubServiceReturns();

        // then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> pathService.findShortestPath(교대역.getId(), 오남역.getId()));
        assertThatIllegalArgumentException()
                .isThrownBy(() -> pathService.findShortestPath(오남역.getId(), 교대역.getId()));
    }

    private void stubServiceReturns() {
        List<String> mockStationNames = Arrays.asList(
                "교대역", "강남역", "역삼역", "선릉역", "한티역", "남부터미널역", "양재역", "매봉역", "도곡역", "오남역", "진접역"
        );
        List<Station> mockStations = Arrays.asList(
                교대역, 강남역, 역삼역, 선릉역, 한티역, 남부터미널역, 양재역, 매봉역, 도곡역, 오남역, 진접역
        );

        when(lineService.findLines()).thenReturn(Arrays.asList(이호선, 삼호선, 사호선, 신분당선, 수인분당선));
        for (int idx = 0; idx < mockStations.size(); idx++) {
            lenient().when(mockStations.get(idx).getId()).thenReturn(idx + 1L);
            lenient().when(mockStations.get(idx).getName()).thenReturn(mockStationNames.get(idx));
            lenient().when(stationService.findById(idx + 1L)).thenReturn(mockStations.get(idx));
        }
    }

    private void 최소_노선_경로_일치됨(PathResponse.ShortestPath source, Station... target) {
        List<PathResponse.PathStation> stations = source.getStations();

        assertThat(stations.size()).isEqualTo(target.length);

        for (int idx = 0; idx < stations.size(); idx++) {
            assertThat(stations.get(idx).getName()).isEqualTo(target[idx].getName());
        }
    }

    private void 최소_노선_길이_일치됨(int sourceDistance, int targetDistance) {
        assertThat(sourceDistance).isEqualTo(targetDistance);
    }
}
