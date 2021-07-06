package nextstep.subway.path.domain;

import nextstep.subway.exception.SectionNotConnectedException;
import nextstep.subway.exception.StationNotExistException;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Lines;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.*;

class PathFinderTest {
    private static final Station 강남역 = Station.of(1L, "강남역");
    private static final Station 양재역 = Station.of(2L, "양재역");
    private static final Station 교대역 = Station.of(3L, "교대역");
    private static final Station 남부터미널역 = Station.of(4L, "남부터미널역");
    private static final Station 몽촌토성역 = Station.of(5L, "몽촌토성역");
    private static final Station 오금역 = Station.of(6L, "오금역");

    private static final Line 신분당선 = new Line("신분당선", "bg-red-600", 강남역, 양재역, 10);
    private static final Line 이호선 = new Line("이호선", "bg-green-600", 교대역, 강남역, 10);
    private static final Line 삼호선 = new Line("삼호선", "bg-orange-600", 교대역, 양재역, 5);
    private static final Line 팔호선 = new Line("팔호선", "bg-pink-600", 몽촌토성역, 오금역, 20);

    private Lines 전체_라인;

    @BeforeEach
    void setUp() {
        전체_라인 = new Lines(Arrays.asList(신분당선, 이호선, 삼호선, 팔호선));
    }

    @DisplayName("최단 경로를 조회한다. (목적지로 가는 역을 순서대로 반환한다)")
    @Test
    void findShortestPath() {
        // given
        삼호선.addSection(남부터미널역, 양재역, 2);
        PathFinder pathFinder = PathFinder.init(전체_라인);

        // when
        Path 최단경로 = pathFinder.findShortestPath(교대역, 양재역);

        // then
        assertThat(최단경로).isEqualTo(new Path(Arrays.asList(교대역, 남부터미널역, 양재역), 5));
    }

    @DisplayName("연결되어있지 않은 출발역과 도착역으로 최단 경로를 조회한다.")
    @Test
    void findShortestPath_notConnectedSection() {
        // given
        PathFinder pathFinder = PathFinder.init(전체_라인);

        // when & then
        assertThatThrownBy(() -> pathFinder.findShortestPath(교대역, 몽촌토성역))
                .isInstanceOf(SectionNotConnectedException.class)
                .hasMessage("구간이 연결되어 있지 않습니다.");
    }

    @DisplayName("존재하지 않는 역으로 최단 경로를 조회한다.")
    @Test
    void findShortestPath_notFoundStation() {
        // given
        Station 천호역 = Station.of(7L, "천호역");
        PathFinder pathFinder = PathFinder.init(전체_라인);

        // when & then
        assertThatThrownBy(() -> pathFinder.findShortestPath(교대역, 천호역))
                .isInstanceOf(StationNotExistException.class)
                .hasMessage("요청한 역이 존재하지 않습니다.");
    }
}
