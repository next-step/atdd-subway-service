package nextstep.subway.path.application;

import static java.util.stream.Collectors.*;
import static nextstep.subway.auth.acceptance.AuthTest.*;
import static nextstep.subway.line.domain.LineTest.*;
import static nextstep.subway.station.domain.StationTest.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import nextstep.subway.line.application.LineService;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.dto.StationResponse;

@DisplayName("경로 찾기 단위 테스트")
@ExtendWith(MockitoExtension.class)
class PathServiceTest {

    private PathService pathService;

    @Mock
    LineService lineService;
    @Mock
    StationService stationService;

    @BeforeEach
    void setUp() {
        pathService = new PathService(lineService, stationService);

        // given
        when(lineService.findLinesEntities()).thenReturn(Stream.of(신분당선, 이호선).collect(toList()));
    }

    @Test
    @DisplayName("최단 경로를 찾고 결과 확인")
    void getShortestPath() {
        // given
        when(stationService.findStationById(양재역.getId())).thenReturn(양재역);
        when(stationService.findStationById(교대역.getId())).thenReturn(교대역);

        // when
        PathResponse pathResponse = pathService.getShortestPath(로그인_일반_사용자, 양재역.getId(), 교대역.getId());

        // then
        StationResponse[] expectedStations = Stream.of(양재역, 강남역, 교대역)
            .map(StationResponse::of)
            .toArray(StationResponse[]::new);
        assertThat(pathResponse.getFare()).isEqualTo(3350);
        assertThat(pathResponse.getDistance()).isEqualTo(130);
        assertThat(pathResponse.getStations()).containsExactly(expectedStations);
    }

    @Test
    @DisplayName("노선 추가 요금 확인")
    void getLineAdditionalFare() {
        // given
        when(stationService.findStationById(양재역.getId())).thenReturn(양재역);
        when(stationService.findStationById(강남역.getId())).thenReturn(강남역);
        when(stationService.findStationById(교대역.getId())).thenReturn(교대역);

        // when
        PathResponse pathResponse1
            = pathService.getShortestPath(로그인_일반_사용자, 양재역.getId(), 강남역.getId());

        // then
        assertThat(pathResponse1.getFare()).isEqualTo(3050);

        // when
        PathResponse pathResponse2
            = pathService.getShortestPath(로그인_일반_사용자, 강남역.getId(), 교대역.getId());

        // then
        assertThat(pathResponse2.getFare()).isEqualTo(1650);
    }

    @Test
    @DisplayName("청소년 요금 확인")
    void getFareForTeenagers() {
        // given
        when(stationService.findStationById(강남역.getId())).thenReturn(강남역);
        when(stationService.findStationById(교대역.getId())).thenReturn(교대역);

        // when
        PathResponse pathResponse = pathService.getShortestPath(로그인_청소년_사용자, 강남역.getId(), 교대역.getId());

        // then1650
        assertThat(pathResponse.getFare()).isEqualTo(1040);
    }

    @Test
    @DisplayName("어린이 요금 확인")
    void getFarePathForChildren() {
        // given
        when(stationService.findStationById(강남역.getId())).thenReturn(강남역);
        when(stationService.findStationById(교대역.getId())).thenReturn(교대역);

        // when
        PathResponse pathResponse = pathService.getShortestPath(로그인_어린이_사용자, 강남역.getId(), 교대역.getId());

        // then
        assertThat(pathResponse.getFare()).isEqualTo(650);
    }
}
