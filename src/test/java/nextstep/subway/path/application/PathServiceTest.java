package nextstep.subway.path.application;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.path.ui.PathResponse;
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
import java.util.Optional;

import static nextstep.subway.Fixture.*;
import static nextstep.subway.path.acceptance.PathAcceptanceStep.최단_경로_조회_요청;
import static nextstep.subway.path.acceptance.PathAcceptanceStep.최단경로_목록_조회_실패;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@DisplayName("지하철 경로 관련 서비스 테스트")
@ExtendWith(MockitoExtension.class)
public class PathServiceTest {

    @InjectMocks
    PathService pathService;

    @Mock
    private LineRepository lineRepository;

    @Mock
    private StationRepository stationRepository;

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
     * 남부터미널역  --- *3호선* ---   양재
     */

    @BeforeEach
    public void setUp() {
        강남역 = createStation("강남역", 1L);
        양재역 = createStation("양재역", 2L);
        교대역 = createStation("교대역", 3L);
        남부터미널역 = createStation("남부터미널역", 4L);
        신분당선 = createLine("신분당선", "bg-red-600", 강남역, 양재역, 10);
        이호선 = createLine("이호선", "bg-red-600", 교대역, 강남역, 10);
        삼호선 = createLine("삼호선", "bg-red-600", 교대역, 양재역, 5);
        삼호선.addSection(createSection(교대역, 남부터미널역, 3));
    }


    @DisplayName("최단 경로 조회에 성공한다.")
    @Test
    void findShortestPath() {
        when(stationRepository.findById(강남역.getId())).thenReturn(Optional.of(강남역));
        when(stationRepository.findById(남부터미널역.getId())).thenReturn(Optional.of(남부터미널역));
        when(lineRepository.findAll()).thenReturn(Arrays.asList(신분당선, 이호선, 삼호선));

        PathResponse shortestPath = pathService.findShortestPath(강남역.getId(), 남부터미널역.getId());

        assertThat(shortestPath.getStations().stream().map(StationResponse::getId))
                .containsExactly(강남역.getId(), 양재역.getId(), 남부터미널역.getId());
        assertThat(shortestPath.getDistance()).isEqualTo(12);
    }

    @DisplayName("최단 경로를 조회 시, 출발역과 도착역이 같으면 예외를 반환한다.")
    @Test
    void getLinesWithException() {
        when(stationRepository.findById(강남역.getId())).thenReturn(Optional.of(강남역));
        when(lineRepository.findAll()).thenReturn(Arrays.asList(신분당선, 이호선, 삼호선));

        assertThatThrownBy(() -> pathService.findShortestPath(강남역.getId(), 강남역.getId()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("출발역과 도착역이 " + 강남역.getName() + "으로 동일합니다.");
    }
}
