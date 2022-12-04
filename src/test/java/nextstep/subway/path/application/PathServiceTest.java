package nextstep.subway.path.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import nextstep.subway.line.application.LineService;
import nextstep.subway.path.domain.PathFinder;
import nextstep.subway.path.dto.PathFinderResult;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.path.dto.PathResponse.PathStationResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;

@ExtendWith(MockitoExtension.class)
class PathServiceTest {
    @Mock
    private PathFinder pathFinder;
    @Mock
    private LineService lineService;
    @Mock
    private StationService stationService;

    private Station 강남역;
    private Station 역삼역;

    @BeforeEach
    public void setUp() {
        강남역 = new Station(1L, "강남역");
        역삼역 = new Station(2L, "역삼역");
    }

    @DisplayName("최단경로 조회 시 결과로 Station 리스트와 최단거리를 반환")
    @Test
    void find1() {
        PathService pathService = new PathService(pathFinder, lineService, stationService);
        PathFinderResult pathFinderResult = new PathFinderResult(Arrays.asList(1L, 2L), 10);
        when(pathFinder.find(any(), any(), any())).thenReturn(pathFinderResult);
        when(stationService.findStationById(eq(1L))).thenReturn(강남역);
        when(stationService.findStationById(eq(2L))).thenReturn(역삼역);

        PathResponse pathResult = pathService.findPath(1L, 2L);

        assertThat(pathResult.getDistance()).isEqualTo(10);
        eqStation(pathResult.getStations().get(0), 강남역);
        eqStation(pathResult.getStations().get(1), 역삼역);
    }

    private void eqStation(PathStationResponse stationResponse, Station station) {
        assertThat(stationResponse.getId()).isEqualTo(station.getId());
        assertThat(stationResponse.getName()).isEqualTo(station.getName());
        assertThat(stationResponse.getCreatedAt()).isEqualTo(station.getCreatedDate());
    }
}
