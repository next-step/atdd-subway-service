package nextstep.subway.path.application;

import nextstep.subway.exception.PathNotFoundException;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.domain.Section;
import nextstep.subway.message.ExceptionMessage;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.assertj.core.api.Assertions;
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
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.when;


@DisplayName("지하철 최단 경로 조회 서비스 테스트")
@ExtendWith(MockitoExtension.class)
class PathServiceTest {

    @Mock
    private LineRepository lineRepository;

    @Mock
    private StationRepository stationRepository;

    private Line 신분당선;
    private Line 분당선;
    private Line 삼호선;
    private Station 정자역;
    private Station 양재역;
    private Station 수서역;
    private Station 서현역;

    /**
     * 양재역 ------*3호선(5)*------ 수서역
     * ㅣ                          ㅣ
     * ㅣ                          ㅣ
     * *신분당선(10)*             *분당선(5)*
     * ㅣ                          ㅣ
     * ㅣ                          ㅣ
     * 정쟈역 ------*분당선(5)*------ 서현역
     */
    @BeforeEach
    void setUp() {
        정자역 = new Station("정자역");
        양재역 = new Station("양재역");
        수서역 = new Station("수서역");
        서현역 = new Station("서현역");

        신분당선 = new Line("신분당선", "red", 양재역, 정자역, 10);
        분당선 = new Line("분당선", "yellow", 수서역, 정자역, 10);
        삼호선 = new Line("삼호선", "orange", 양재역, 수서역, 5);

        분당선.addSection(new Section(분당선, 서현역, 정자역, 5));
    }

    @InjectMocks
    private PathService pathService;

    @DisplayName("출발역(양재역)과 도착역(서현역) 사이의 최단 경로 찾기")
    @Test
    void findShortestPath() {
        when(stationRepository.findById(1L)).thenReturn(Optional.of(양재역));
        when(stationRepository.findById(2L)).thenReturn(Optional.of(서현역));
        when(lineRepository.findAll()).thenReturn(Arrays.asList(신분당선, 분당선, 삼호선));

        PathResponse response = pathService.findShortestPath(1L, 2L);

        assertAll(
                () -> assertThat(response.getStations()).hasSize(3),
                () -> assertThat(response.getDistance()).isEqualTo(10)
        );
    }

    @DisplayName("출발역과 도착역이 같은 경우 예외가 발생한다.")
    @Test
    void findShortestPathException1() {
        when(stationRepository.findById(1L)).thenReturn(Optional.of(양재역));
        when(stationRepository.findById(1L)).thenReturn(Optional.of(양재역));
        when(lineRepository.findAll()).thenReturn(Arrays.asList(신분당선, 분당선, 삼호선));

        Assertions.assertThatThrownBy(() -> pathService.findShortestPath(1L, 1L))
                .isInstanceOf(PathNotFoundException.class)
                .hasMessageStartingWith(ExceptionMessage.SOURCE_AND_TARGET_EQUAL);
    }
}
