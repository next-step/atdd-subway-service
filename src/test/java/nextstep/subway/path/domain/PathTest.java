package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.StationFixtures;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.*;

public class PathTest {

    /**
     *              거리 10
     * 교대역    --- *2호선* ---   강남역
     * |                        |
     * *3호선*                   *신분당선*
     * 거리 3                     거리 10
     * |                        |
     * 남부터미널역  --- *3호선* --- 양재역
     *              거리 2
     */
    public static Path path;

    @BeforeAll
    static void setUp() {
        Line 이호선 = new Line("이호선", "bg-green-600", StationFixtures.교대역, StationFixtures.강남역, 10);
        Line 삼호선 = new Line("삼호선", "bg-orange-600", StationFixtures.교대역, StationFixtures.남부터미널역, 3);
        Line 신분당선 = new Line("신분당선", "bg-red-600", StationFixtures.강남역, StationFixtures.양재역, 10);

        Section 남부터미널역_양재역 = Section.builder().line(삼호선)
                .upStation(StationFixtures.남부터미널역)
                .downStation(StationFixtures.양재역)
                .distance(new Distance(2))
                .build();
        삼호선.add(남부터미널역_양재역);

        path = Path.of(Arrays.asList(이호선, 삼호선, 신분당선));
    }

    @DisplayName("최단 경로 조회")
    @Test
    void findShortestPath() {
        // when
        ShortestPath shortestPath = path.findShortestPath(StationFixtures.강남역, StationFixtures.남부터미널역);

        // then
        assertThat(shortestPath.getStations())
                .map(Station::getName)
                .containsExactly("강남역", "양재역", "남부터미널역");
    }

    @DisplayName("출발지와 도착지가 서로 동일할 경우")
    @Test
    void findPathSameStations() {
        // when & then
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> {
            path.findShortestPath(StationFixtures.강남역, StationFixtures.강남역);
        }).withMessageMatching("조회하려는 출발지와 도착지가 같습니다.");
    }

    @DisplayName("존재하지 않은 출발역이나 도착역을 조회 할 경우")
    @Test
    void findNotExistStations() {
        // when & then
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> {
            path.findShortestPath(StationFixtures.강남역, StationFixtures.천호역);
        }).withMessageMatching("경로에 포함되어 있지 않은 역입니다.");
    }

}
