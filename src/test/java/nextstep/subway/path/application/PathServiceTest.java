package nextstep.subway.path.application;

import nextstep.subway.constant.ErrorCode;
import nextstep.subway.fixture.LineTestFactory;
import nextstep.subway.fixture.SectionTestFactory;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@DisplayName("지하철 경로 관련 서비스 테스트")
@ExtendWith(MockitoExtension.class)
class PathServiceTest {
    @Mock
    private StationRepository stationRepository;

    @Mock
    private LineRepository lineRepository;

    @InjectMocks
    private PathService pathService;

    private Station 인천역;
    private Station 부평역;
    private Station 연신내역;
    private Station 불광역;
    private Station 판교역;
    private Station 양재역;
    private Line 일호선;
    private Line 삼호선;
    private Line 신분당선;

    @BeforeEach
    public void setUp() {
        인천역 = new Station("인천역");
        부평역 = new Station("부평역");
        연신내역 = new Station("연신내역");
        불광역 = new Station("불광역");
        판교역 = new Station("판교역");
        양재역 = new Station("양재역");
        일호선 = LineTestFactory.create("일호선", "bg-red-600", 인천역, 부평역, 10, 0);
        삼호선 = LineTestFactory.create("삼호선", "bg-red-500", 연신내역, 불광역, 10, 0);
        신분당선 = LineTestFactory.create("신분당선", "bg-red-400", 판교역, 양재역, 10, 0);
        일호선.addSection(SectionTestFactory.create(부평역, 연신내역, 10));
    }

    @DisplayName("두 역간의 최단 경로를 구할 수 있다")
    @Test
    void findShortestPath() {
        // given
        Long source = 1L;
        Long target = 3L;
        when(stationRepository.findById(source)).thenReturn(Optional.of(인천역));
        when(stationRepository.findById(target)).thenReturn(Optional.of(불광역));
        when(lineRepository.findAll()).thenReturn(Arrays.asList(일호선, 삼호선, 신분당선));

        // when
        PathResponse response = pathService.findShortestPath(source, target, 33);

        // then
        assertThat(response.getDistance()).isEqualTo(30);

        // then
        List<String> actualIds = response.getStations()
                .stream()
                .map(StationResponse::getName)
                .collect(Collectors.toList());
        assertThat(actualIds).containsExactlyElementsOf(Arrays.asList("인천역", "부평역", "연신내역", "불광역"));
    }

    @DisplayName("출발역과 도착역이 동일하면 예외가 발생한다")
    @Test
    void sameStationException() {
        // given
        Long source = 1L;
        when(stationRepository.findById(source)).thenReturn(Optional.of(인천역));
        when(lineRepository.findAll()).thenReturn(Arrays.asList(일호선, 삼호선, 신분당선));

        // when & then
        assertThatThrownBy(() -> pathService.findShortestPath(source, source, 33))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(ErrorCode.FIND_PATH_SAME_SOURCE_TARGET.getMessage());
    }

    @DisplayName("최단경로를 조회하는 역이 존재하지 않으면 예외가 발생한다")
    @Test
    void notExistStationException() {
        // given
        Long source = 1L;
        Long target = 3L;
        Station 존재하지_않는_역 = new Station("존재하지 않는 역");
        when(stationRepository.findById(source)).thenReturn(Optional.of(존재하지_않는_역));
        when(stationRepository.findById(target)).thenReturn(Optional.of(인천역));
        when(lineRepository.findAll()).thenReturn(Arrays.asList(일호선, 삼호선, 신분당선));

        // when & then
        assertThatThrownBy(() -> pathService.findShortestPath(source, target, 33))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(ErrorCode.FIND_PATH_NOT_EXIST.getMessage());
    }

    @DisplayName("출발역과 도착역이 연결 되어 있지 않으면 예외가 발생한다")
    @Test
    void notConnectException() {
        // given
        Long source = 1L;
        Long target = 3L;
        when(stationRepository.findById(source)).thenReturn(Optional.of(인천역));
        when(stationRepository.findById(target)).thenReturn(Optional.of(판교역));
        when(lineRepository.findAll()).thenReturn(Arrays.asList(일호선, 삼호선, 신분당선));

        // when & then
        assertThatThrownBy(() -> pathService.findShortestPath(source, target, 33))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(ErrorCode.FIND_PATH_NOT_CONNECT.getMessage());
    }
}
