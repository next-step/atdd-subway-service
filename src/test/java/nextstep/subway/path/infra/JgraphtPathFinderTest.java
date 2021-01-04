package nextstep.subway.path.infra;

import nextstep.subway.path.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class JgraphtPathFinderTest {

    private PathFinder pathFinder;

    private PathStation 교대역;
    private PathStation 강남역;
    private PathStation 남부터미널역;
    private PathStation 양재역;

    private PathSections pathSections;

    @BeforeEach
    void setUp() {
        pathFinder = new JgraphtPathFinder();
        교대역 = new PathStation(1L, "교대역", LocalDateTime.now());
        강남역 = new PathStation(2L, "강남역", LocalDateTime.now());
        남부터미널역 = new PathStation(3L, "남부터미널역", LocalDateTime.now());
        양재역 = new PathStation(4L, "양재역", LocalDateTime.now());

        PathSection pathSection1 = new PathSection(교대역, 강남역, 10);
        PathSection pathSection2 = new PathSection(강남역, 양재역, 10);
        PathSection pathSection3 = new PathSection(남부터미널역, 양재역, 5);
        PathSection pathSection4 = new PathSection(교대역, 남부터미널역, 3);

        pathSections = new PathSections(Arrays.asList(
                pathSection1,
                pathSection2,
                pathSection3,
                pathSection4
        ));
    }

    /**
     * --- *2호선* ---
     * 교대역 - 강남역
     */
    @DisplayName("노선 하나의 지하철역에서 최단거리를 구한다.")
    @Test
    void singleLineShortestPath() {
        // when
        Path shortest = pathFinder.findShortest(pathSections, 교대역, 강남역);

        // then
        assertThat(shortest.getPathStations()).contains(교대역, 강남역);
        assertThat(shortest.getDistance()).isEqualTo(10);
    }

    /**
     * 교대역    --- *2호선* ---   강남역
     * |                        |
     * *3호선*                   *신분당선*
     * |                        |
     * 남부터미널역  --- *3호선* --- 양재역
     */
    @DisplayName("노선 여러개의 지하철역에서 최단거리를 구한다.")
    @Test
    void multiLineShortestPath() {
        // when
        Path shortest = pathFinder.findShortest(pathSections, 강남역, 남부터미널역);

        // then
        assertThat(shortest.getPathStations()).containsExactly(강남역, 교대역, 남부터미널역);
        assertThat(shortest.getDistance()).isEqualTo(13);
    }

    @DisplayName("출발역과 도착역이 같은 경우를 예외처리 한다.")
    @Test
    void samePathStation() {
        // when / then
        assertThrows(IllegalArgumentException.class, () -> pathFinder.findShortest(pathSections, 교대역, 교대역));
    }

    /**
     * 교대역    --- *2호선* ---   강남역
     * <p>
     * 남부터미널역  --- *3호선* --- 양재역
     */
    @DisplayName("출발역과 도착역이 연결이 되어 있지 않은 경우 예외처리 한다.")
    @Test
    void notConnectedPathStation() {
        // given
        PathSection pathSection1 = new PathSection(교대역, 강남역, 10);
        PathSection pathSection2 = new PathSection(남부터미널역, 양재역, 5);
        PathSections newPathSections = new PathSections(Arrays.asList(pathSection1, pathSection2));

        // when /then
        assertThrows(IllegalArgumentException.class, () -> pathFinder.findShortest(newPathSections, 교대역, 양재역));
    }

    @DisplayName("존재하지 않은 출발역이나 도착역을 조회 할 경우 예외처리 한다.")
    @Test
    void notExistedPathStation() {
        // given
        PathStation 신도림역 = new PathStation(10L, "신도림역", LocalDateTime.now());
        PathStation 당정역 = new PathStation(11L, "당정역", LocalDateTime.now());

        // when / then
        assertThrows(IllegalArgumentException.class, () -> pathFinder.findShortest(pathSections, 신도림역, 당정역));
        assertThrows(IllegalArgumentException.class, () -> pathFinder.findShortest(pathSections, 강남역, 당정역));
        assertThrows(IllegalArgumentException.class, () -> pathFinder.findShortest(pathSections, 신도림역, 남부터미널역));
    }
}
