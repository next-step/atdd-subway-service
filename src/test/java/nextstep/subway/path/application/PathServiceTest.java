package nextstep.subway.path.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.swing.text.html.Option;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.domain.Section;
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

@DisplayName("지하철 경로 관련 서비스 테스트")
@ExtendWith(MockitoExtension.class)
public class PathServiceTest {

    @Mock
    private StationRepository stationRepository;

    @Mock
    private LineRepository lineRepository;

    @InjectMocks
    private PathService pathService;

    private Line 신분당선;
    private Line 이호선;
    private Line 삼호선;
    private Line 오호선;
    private Station 강남역;
    private Station 양재역;
    private Station 교대역;
    private Station 남부터미널역;
    private Station 마곡역;
    private Station 마포역;

    /**
     * 교대역    --- *2호선* ---   강남역
     * |                        |
     * *3호선*                   *신분당선*
     * |                        |
     * 남부터미널역  --- *3호선* ---   양재
     *
     * 마곡역 --- *5호선* --- 마포역
     */
    @BeforeEach
    public void setUp() {
        강남역 = new Station("강남역");
        양재역 = new Station("양재역");
        교대역 = new Station("교대역");
        남부터미널역 = new Station("남부터미널역");
        마곡역 = new Station("마곡역");
        마포역 = new Station("마포역");

        신분당선 = new Line("신분당선", "bg-red-600", 강남역, 양재역, 10);
        이호선 = new Line("이호선", "bg-red-600", 교대역, 강남역, 10);
        삼호선 = new Line("삼호선", "bg-red-600", 교대역, 양재역, 5);
        오호선 = new Line("오호선", "bg-red-600", 마곡역, 마포역, 5);

        삼호선.addSection(Section.of(삼호선, 교대역, 남부터미널역, 3));
    }

    @DisplayName("출발역과 도착역의 최단거리를 조회한다.")
    @Test
    void findShortestPath() {
        Long source = 1L;
        Long target = 2L;
        when(stationRepository.findById(source)).thenReturn(Optional.of(강남역));
        when(stationRepository.findById(target)).thenReturn(Optional.of(남부터미널역));
        when(lineRepository.findAll()).thenReturn(Arrays.asList(신분당선, 삼호선, 신분당선));

        PathResponse response = pathService.findShortestPath(source, target, 20);

        assertAll(
            () -> assertThat(response.getDistance()).isEqualTo(12),
            () -> assertThat(response.getStations()
                .stream()
                .map(StationResponse::getName)
                .collect(Collectors.toList()))
                .hasSize(3)
                .containsExactly("강남역", "양재역", "남부터미널역")
        );
    }

    @DisplayName("출발역과 도착역이 동일한 경우 에러가 발생된다.")
    @Test
    void validateSameStationException() {
        Long source = 1L;
        when(stationRepository.findById(source)).thenReturn(Optional.of(강남역));
        when(lineRepository.findAll()).thenReturn(Arrays.asList(신분당선, 삼호선, 신분당선));

        assertThatThrownBy(() -> pathService.findShortestPath(source, source, 20))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("출발역과 도착역이 연결이 되어 있지 않은 경우 에러가 발생된다.")
    @Test
    void validateNotConnectException() {
        Long source = 1L;
        Long target = 2L;
        when(stationRepository.findById(source)).thenReturn(Optional.of(강남역));
        when(stationRepository.findById(target)).thenReturn(Optional.of(마곡역));
        when(lineRepository.findAll()).thenReturn(Arrays.asList(신분당선, 삼호선, 신분당선, 오호선));

        assertThatThrownBy(() -> pathService.findShortestPath(source, target, 20))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("존재하지 않은 출발역이나 도착역을 조회할 경우 에러가 발생된다.")
    @Test
    void validateNotExistStationException() {
        Long source = 1L;
        Long target = 2L;
        when(stationRepository.findById(source)).thenReturn(Optional.of(마곡역));
        when(stationRepository.findById(target)).thenReturn(Optional.of(마포역));
        when(lineRepository.findAll()).thenReturn(Arrays.asList(신분당선, 삼호선, 신분당선));

        assertThatThrownBy(() -> pathService.findShortestPath(source, target, 20))
            .isInstanceOf(IllegalArgumentException.class);
    }
}
