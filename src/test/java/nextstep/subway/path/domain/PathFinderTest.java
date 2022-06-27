package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Line;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class PathFinderTest {
    private Station 강남역;
    private Station 광교역;
    private Station 판교역;
    private Station 신림역;
    private Line 신분당선;
    private PathFinder pathFinder;

    @BeforeEach
    void setUp() {
        강남역 = new Station(1L, "강남역");
        광교역 = new Station(2L, "광교역");
        판교역 = new Station(3L, "판교역");
        신림역 = new Station(4L,"신림역");
        신분당선 = new Line("신분당선", "bg-red-600", 강남역, 광교역, 10);
        pathFinder = new PathFinder(Arrays.asList(신분당선));
    }

    @Test
    void 경로_리스트_포함_역_확인() {
        // when
        List<Long> stations = pathFinder.getStationsId(강남역, 광교역);

        // then
        assertThat(stations).containsExactly(강남역.getId(), 광교역.getId());
    }

    @Test
    void 경로_길이_조회() {
        // when
        double distance = pathFinder.getDistance(강남역, 광교역);

        // then
        assertThat(distance).isEqualTo(10);
    }

    @DisplayName("출발역과 도착역이 같은 경우 예외 발생")
    @Test
    void sameSourceAndTarget() {
        assertThatThrownBy(() -> {
            pathFinder.getStationsId(강남역, 강남역);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("출발역과 도착역이 연결되어 있지 않은 경우 예외 발생")
    @Test
    void notConnectSourceAndTarget() {
        assertThatThrownBy(() -> {
            pathFinder.getStationsId(강남역, 판교역);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("존재하지 않는 출발역이나 도착역을 조회하는 경우 예외 발생")
    @Test
    void notContainsSourceOrTarget() {
        assertThatThrownBy(() -> {
            pathFinder.getStationsId(신림역, 판교역);
        }).isInstanceOf(IllegalArgumentException.class);
    }
}
