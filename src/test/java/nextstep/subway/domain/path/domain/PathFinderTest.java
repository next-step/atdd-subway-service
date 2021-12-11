package nextstep.subway.domain.path.domain;

import nextstep.subway.domain.line.domain.Distance;
import nextstep.subway.domain.line.domain.Line;
import nextstep.subway.domain.path.dto.PathFinderRequest;
import nextstep.subway.domain.path.exception.NotConnectedStation;
import nextstep.subway.domain.path.exception.SameDepartureAndArrivalStationException;
import nextstep.subway.domain.path.exception.StationNotFoundException;
import nextstep.subway.domain.station.domain.Station;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PathFinderTest {

    @Test
    @DisplayName("최단 경로 조회")
    void findPaths() {
        // given
        final Station 강남역 = new Station(1L, "강남역");
        final Station 양재역 = new Station(2L, "양재역");
        final Station 교대역 = new Station(3L, "교대역");
        final Station 남부터미널역 = new Station(4L, "남부터미널역");
        final Line 신분당선 = new Line("신분당선", "bg-red-600", 강남역, 양재역, new Distance(10));
        final Line 이호선 = new Line("이호선", "bg-green-400", 교대역, 강남역, new Distance(10));
        final Line 삼호선 = new Line("삼호선", "bg-orange-200", 교대역, 양재역, new Distance(5));
        삼호선.addSection(교대역, 남부터미널역, new Distance(3));

        // when
        final PathFinder pathFinder = new PathFinder(Arrays.asList(신분당선, 이호선, 삼호선));
        final Route shortestRoute = pathFinder.findShortestRoute(교대역.getId(), 양재역.getId());

        // then
        assertAll(() -> {
            assertThat(shortestRoute.getStations()).extracting("name").containsExactly("교대역","남부터미널역","양재역");
            assertThat(shortestRoute.getDistance()).isEqualTo(5);
        });
    }

    @Test
    @DisplayName("출발역과 도착역이 동일")
    void sameDepartureAndArrivalStation() {
        // given
        final Station 강남역 = new Station(1L, "강남역");
        final Station 교대역 = new Station(2L, "교대역");
        final Line 이호선 = new Line("이호선", "bg-green-400", 교대역, 강남역, new Distance(10));

        // when
        final PathFinder pathFinder = new PathFinder(Arrays.asList(이호선));
        assertThrows(SameDepartureAndArrivalStationException.class,
                () -> pathFinder.findShortestRoute(교대역.getId(), 교대역.getId()));
    }

    @Test
    @DisplayName("출발역과 도착역 미연결")
    void stationNotConnected() {
        // given
        final Station 강남역 = new Station(1L, "강남역");
        final Station 교대역 = new Station(2L, "교대역");
        final Station 동작역 = new Station(3L, "동작역");
        final Station 신논현역 = new Station(4L, "신논현역");
        final Line 이호선 = new Line("이호선", "bg-green-400", 교대역, 강남역, new Distance(10));
        final Line 구호선 = new Line("구호선", "bg-partridge-400", 동작역, 신논현역, new Distance(10));

        final PathFinder pathFinder = new PathFinder(Arrays.asList(이호선, 구호선));

        // when
        assertThrows(NotConnectedStation.class,
                () -> pathFinder.findShortestRoute(교대역.getId(), 동작역.getId()));
    }

    @Test
    @DisplayName("존재하지 않는 역")
    void stationNotFound() {
        // given
        final Station 강남역 = new Station(1L, "강남역");
        final Station 교대역 = new Station(2L, "교대역");
        final Line 이호선 = new Line("이호선", "bg-green-400", 교대역, 강남역, new Distance(10));

        final PathFinder pathFinder = new PathFinder(Arrays.asList( 이호선));

        // when
        final PathFinderRequest pathFinderRequest = new PathFinderRequest(교대역.getId(), 100L);
        assertThrows(StationNotFoundException.class,
                () -> pathFinder.findShortestRoute(교대역.getId(), 100L));
    }
}
