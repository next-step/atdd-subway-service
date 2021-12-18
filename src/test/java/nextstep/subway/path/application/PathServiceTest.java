package nextstep.subway.path.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.util.Arrays;
import nextstep.subway.path.domain.Path;
import nextstep.subway.path.domain.JgraphtPathFinder;
import nextstep.subway.path.dto.PathRequest;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("지하철 경로 조회")
@ExtendWith(MockitoExtension.class)

public class PathServiceTest {

    private static final Long TEST_SRC_STATION_ID = 1L;
    private static final Long TEST_DEST_STATION_ID = 2L;

    @Mock
    private JgraphtPathFinder pathFinder;

    @Mock
    private StationService stationService;

    @InjectMocks
    private PathService pathService;

    private void given_정상적으로_역을_조회() {
        given_정상적으로_출발역을_조회();
        given_정상적으로_도착역을_조회();
    }

    private void given_정상적으로_출발역을_조회() {
        given(stationService.findById(TEST_SRC_STATION_ID))
            .willReturn(new Station("양재역"));
    }

    private void given_정상적으로_도착역을_조회() {
        given(stationService.findById(TEST_DEST_STATION_ID))
            .willReturn(new Station("강남역"));
    }

    private void given_정상적으로_출발역과_도착역이_경로내_연결되어_있음() {
        given(pathFinder.containsStation(any())).willReturn(true);
        given(pathFinder.stationsConnected(any(), any())).willReturn(true);
    }

    private void given_출발역과_도착역이_연결되어있지_않음() {
        given(pathFinder.stationsConnected(any(), any()))
            .willReturn(false);
    }

    private void given_역이_경로내에_존재함() {
        given(pathFinder.containsStation(any()))
            .willReturn(true);
    }

    private void given_역이_경로내에_존재하지_않음() {
        given(pathFinder.containsStation(any()))
            .willReturn(false);
    }

    private void given_정상적으로_최단경로를_반환() {
        given(pathFinder.getShortestPath(any(), any()))
            .willReturn(new Path(Arrays.asList(new Station()), 10.0));
    }

    @DisplayName("역과 역 사이 가장 가까운 경로를 찾는다")
    @Test
    void findShortestPathTest() {
        // given
        given_정상적으로_역을_조회();
        given_정상적으로_출발역과_도착역이_경로내_연결되어_있음();
        given_정상적으로_최단경로를_반환();
        PathRequest request = new PathRequest(TEST_SRC_STATION_ID, TEST_DEST_STATION_ID);

        // when
        PathResponse pathResponse = pathService.findShortestPath(request, pathFinder);

        // then
        assertThat(pathResponse.getPath()).isNotEmpty();
        assertThat(pathResponse.getPathWeight()).isNotNull();
    }

    @DisplayName("출발역과 도착역이 동일하면 예외 발생한다.")
    @Test
    void findShortestPathTest_SameSrcAndDest() {
        // given
        given_정상적으로_출발역을_조회();
        PathRequest request = new PathRequest(TEST_SRC_STATION_ID, TEST_SRC_STATION_ID);

        // when, then
        assertThatThrownBy(() -> pathService.findShortestPath(request, pathFinder))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("출발역과 도착역이 같습니다");
    }

    @DisplayName("역이 경로 내에 존재하지 않으면 예외 발생한다.")
    @Test
    void findShortestPathTest_stationsNotExist() {
        // given
        given_정상적으로_역을_조회();
        given_역이_경로내에_존재하지_않음();
        PathRequest request = new PathRequest(TEST_SRC_STATION_ID, TEST_DEST_STATION_ID);

        // when, then
        assertThatThrownBy(() -> pathService.findShortestPath(request, pathFinder))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("출발역이나 도착역이 노선 내에 존재하지 않습니다");
    }

    @DisplayName("출발역과 도착역이 연결되어있지 않으면 예외 발생한다.")
    @Test
    void findShortestPathTest_stationsNotConnected() {
        // given
        given_정상적으로_역을_조회();
        given_역이_경로내에_존재함();
        given_출발역과_도착역이_연결되어있지_않음();
        PathRequest request = new PathRequest(TEST_SRC_STATION_ID, TEST_DEST_STATION_ID);

        // when, then
        assertThatThrownBy(() -> pathService.findShortestPath(request, pathFinder))
            .isInstanceOf(RuntimeException.class)
            .hasMessageContaining("출발역과 도착역이 연결이 되어 있지 않습니다");
    }

}
