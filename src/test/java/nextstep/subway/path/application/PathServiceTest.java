package nextstep.subway.path.application;

import nextstep.subway.common.ErrorCode;
import nextstep.subway.exception.BadRequestApiException;
import nextstep.subway.exception.NotFoundApiException;
import nextstep.subway.map.application.SubwayMapService;
import nextstep.subway.map.domain.SubwayMap;
import nextstep.subway.path.dto.PathRequest;
import nextstep.subway.station.application.StationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PathServiceTest {

    private static final long SOURCE = 1L;
    private static final long TARGET = 2L;

    private PathService pathService;

    @Mock
    private StationService stationService;

    @Mock
    private SubwayMapService subwayMapService;

    @BeforeEach
    public void setUp() {
        pathService = new PathService(stationService, subwayMapService);
    }

    @DisplayName("최단 경로 조회 실패 - 존재하지 않는 역")
    @Test
    void findShortestPath_failure_notFoundStation() {
        // given
        PathRequest pathRequest = new PathRequest(SOURCE, TARGET);

        when(stationService.findStationById(SOURCE))
                .thenThrow(new NotFoundApiException(ErrorCode.NOT_FOUND_STATION_ID));

        // when & then
        assertThatThrownBy(() -> pathService.findShortestPath(pathRequest))
                .hasMessage(ErrorCode.NOT_FOUND_STATION_ID.toString());
    }

    @DisplayName("최단 경로 조회 실패 - 출발역과 도착역이 연결되어 있지 않음")
    @Test
    void findShortestPath_failure_uncoupledPath() {
        // given
        PathRequest pathRequest = new PathRequest(SOURCE, TARGET);

        when(subwayMapService.getMap()).thenReturn(mock(SubwayMap.class));
        when(subwayMapService.getMap().findShortestPath(any(), any()))
                .thenThrow(new BadRequestApiException(ErrorCode.UNCOUPLED_PATH));

        // when & then
        assertThatThrownBy(() -> pathService.findShortestPath(pathRequest))
                .hasMessage(ErrorCode.UNCOUPLED_PATH.toString());
    }
}
