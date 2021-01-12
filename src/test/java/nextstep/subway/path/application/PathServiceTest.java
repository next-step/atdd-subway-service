package nextstep.subway.path.application;

import nextstep.subway.line.application.LineService;
import nextstep.subway.line.domain.Line;
import nextstep.subway.path.dto.PathRequest;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@DisplayName("경로에 관련한 기능")
@ExtendWith(MockitoExtension.class)
class PathServiceTest {
    @Mock
    private LineService lineService;
    @Mock
    private StationService stationService;

    private PathService pathService;

    private Station 강남역;
    private Station 양재역;
    private Station 교대역;
    private Station 남부터미널;
    private Line 이호선;
    private Line 신분당선;
    private Line 삼호선;

    @BeforeEach
    void beforeEach() {
        pathService = new PathService(stationService, lineService);
        강남역 = new Station("강남역");
        양재역 = new Station("양재역");
        교대역 = new Station("교대역");
        남부터미널 = new Station("남부터미널");
        이호선 = new Line("2호선", "bg-green-200", 교대역, 강남역, 1000L);
        삼호선 = new Line("3호선", "bg-yellow-200", 교대역, 양재역, 500L);
        삼호선.addSection(교대역, 남부터미널, 300L);
        신분당선 = new Line("신분당선", "bg-red-200", 강남역, 양재역, 1000L);
    }

    @DisplayName("최단 경로 찾기")
    @Test
    void findPath() {
        // Given
        given(lineService.findAllLines()).willReturn(Arrays.asList(이호선, 삼호선, 신분당선));
        given(stationService.findById(any())).willReturn(교대역).willReturn(양재역);
        // When
        PathResponse shortestPath = pathService.findPath(new PathRequest(교대역.getId(), 양재역.getId()));
        // Then
        assertAll(
                () -> assertThat(shortestPath.getStations()).isNotNull(),
                () -> assertThat(shortestPath.getStations()).hasSize(3)
                        .extracting(StationResponse::getName)
                        .containsExactly("교대역", "남부터미널", "양재역"),
                () -> assertThat(shortestPath.getDistance()).isEqualTo(500L)
        );
    }
}
