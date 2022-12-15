package nextstep.subway.path.domain;

import nextstep.subway.fixture.LineFixture;
import nextstep.subway.fixture.StationFixture;
import nextstep.subway.line.domain.Distance;
import nextstep.subway.path.message.PathMessage;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

class ShortestPathFinderTest {

    private PathFinder pathFinder;

    @BeforeEach
    void setUp() {
        pathFinder = new ShortestPathFinder(
                Arrays.asList(LineFixture.이호선, LineFixture.신분당선, LineFixture.구호선)
        );
    }

    /**
     * Given 시작역과 도착역이 주어진경우
     * When 최단 경로 조회시
     * Then 최단 경로를 반환한다
     */
    @DisplayName("시작역과 도착역이 주어진 경우 최단 경로를 반환한다")
    @Test
    void find_shortest_path_test() {
        // when
        Path path = pathFinder.findPath(StationFixture.신논현역, StationFixture.교대역);

        // then
        assertAll(
                () -> assertThat(path.getDistance()).isEqualTo(new Distance(20)),
                () -> assertThat(getStationNames(path)).containsExactly("신논현역", "강남역", "교대역")
        );
    }

    @DisplayName("노선에 등록되지 않은 시작역이 주어진 경우 최단 경로 조회시 예외 처리")
    @Test
    void find_shortest_path_with_not_enrolled_source_station_test() {
        // when & then
        assertThatThrownBy(() -> pathFinder.findPath(StationFixture.잠실역, StationFixture.교대역))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(PathMessage.GRAPH_ERROR_NOT_FOUND_SOURCE_STATION.message());
    }

    @DisplayName("노선에 등록되지 않은 도착역이 주어진 경우 최단 경로 조회시 예외 처리")
    @Test
    void find_shortest_path_with_not_enrolled_target_station_test() {
        // when & then
        assertThatThrownBy(() -> pathFinder.findPath(StationFixture.교대역, StationFixture.잠실역))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(PathMessage.GRAPH_ERROR_NOT_FOUND_TARGET_STATION.message());
    }

    @DisplayName("동일한 역이 시작역과 도착역으로 주어진 경우 최단 경로 조회시 예외 처리")
    @Test
    void find_shortest_path_with_same_stations_test() {
        // when & then
        assertThatThrownBy(() -> pathFinder.findPath(StationFixture.교대역, StationFixture.교대역))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(PathMessage.GRAPH_ERROR_SOURCE_AND_TARGET_STATION_IS_EQUALS.message());
    }

    @DisplayName("연결되어 있지않은 출발역 또는 도착역이 주어진 경우 최단 경로 조회시 예외 처리")
    @Test
    void find_shortest_path_with_not_connected_stations_test() {
        // when & then
        assertThatThrownBy(() -> pathFinder.findPath(StationFixture.학동역, StationFixture.교대역))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(PathMessage.GRAPH_ERROR_NOT_CONNECTED_STATIONS.message());
    }

    private List<String> getStationNames(Path path) {
        return path.getStations().stream()
                .map(Station::getName)
                .collect(Collectors.toList());
    }
}
