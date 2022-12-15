package nextstep.subway.path.service;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.when;

@DisplayName("지하철 최단 경로 조회 서비스 테스트")
@ExtendWith(MockitoExtension.class)
public class PathServiceTest {
    @Mock
    private LineRepository lineRepository;

    @Mock
    private StationRepository stationRepository;

    @InjectMocks
    private PathService pathService;

    private Line 신분당선;
    private Line 이호선;
    private Line 삼호선;
    private Line 구호선;
    private Station 강남역;
    private Station 양재역;
    private Station 교대역;
    private Station 남부터미널역;
    private Station 동작역;
    private Station 석촌역;

    /**
     * 교대역 -------- 2호선(10) ------ 강남역
     * ㅣ                               ㅣ
     * ㅣ                               ㅣ
     * 3호선(3)                      신분당선(10)
     * ㅣ                               ㅣ
     * ㅣ                               ㅣ
     * 남부터미널역 ------3호선(2) ------- 양재
     * <p>
     * 동작역 --------- 9호선(13) ------ 석촌역
     */

    @BeforeEach
    void setUp() {
        강남역 = new Station("강남역");
        양재역 = new Station("양재역");
        교대역 = new Station("교대역");
        남부터미널역 = new Station("남부터미널역");
        동작역 = new Station("동작역");
        석촌역 = new Station("석촌역");

        신분당선 = new Line("신분당선", "bg-red-600", 강남역, 양재역, 10);
        이호선 = new Line("이호선", "bg-red-600", 교대역, 강남역, 10);
        삼호선 = new Line("삼호선", "bg-red-600", 교대역, 양재역, 5);
        구호선 = new Line("구호선", "bg-red-600", 동작역, 석촌역, 13);
    }


    @DisplayName("출발역과 도착역 사이 최단 경로 조회")
    @Test
    public void 최단경로조회() {
        // given
        when(stationRepository.findById(1L)).thenReturn(Optional.of(교대역));
        when(stationRepository.findById(2L)).thenReturn(Optional.of(양재역));
        when(lineRepository.findAll()).thenReturn(Arrays.asList(삼호선, 이호선));
        // when
        PathResponse response = pathService.findShortPath(1L, 2L);
        // then
        assertAll(
                () -> assertThat(response.getStations()).hasSize(2),
                () -> assertThat(response.getDistance()).isEqualTo(5)
        );
    }

    @DisplayName("출발역과 도착역이 같은 경우 예외발생")
    @Test
    public void 최단경로조회_예외발생1() {
        // given
        when(stationRepository.findById(1L)).thenReturn(Optional.of(교대역));
        when(stationRepository.findById(1L)).thenReturn(Optional.of(교대역));
        when(lineRepository.findAll()).thenReturn(Arrays.asList(삼호선, 이호선));
        // when && then
        assertThatThrownBy(() -> pathService.findShortPath(1L, 1l))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("출발역과 도착역이 같을 경우 최단 거리를 조회할 수 없습니다.");
    }

    @DisplayName("출발역과 도착역이 연결이 되어 있지 않은 경우 예외발생")
    @Test
    public void 최단경로조회_에외발생2() {
        // given
        when(stationRepository.findById(1L)).thenReturn(Optional.of(강남역));
        when(stationRepository.findById(2L)).thenReturn(Optional.of(석촌역));
        when(lineRepository.findAll()).thenReturn(Arrays.asList(삼호선, 이호선));
        // when && then
        assertThatThrownBy(() -> pathService.findShortPath(1L, 2l))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("존재하지 않는 출발역이나 도착역을 조회 할 경우")
    @Test
    public void 최단경로조회_에외발생3() {
        // given
        when(stationRepository.findById(1L)).thenReturn(Optional.of(교대역));
        when(stationRepository.findById(0L)).thenReturn(Optional.of(동작역));
        // when && then
        assertThatThrownBy(() -> pathService.findShortPath(1L, 0l))
                .isInstanceOf(IllegalArgumentException.class);
    }


}