package nextstep.subway.path.domain;

import nextstep.subway.exception.SectionNotConnectedException;
import nextstep.subway.exception.StationsNotExistException;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

class PathFinderTest {
    private static final Station 강남역 = Station.of(1L, "강남역");
    private static final Station 양재역 = Station.of(2L, "양재역");
    private static final Station 교대역 = Station.of(3L, "교대역");
    private static final Station 남부터미널역 = Station.of(4L, "남부터미널역");

    private static final Line 신분당선 = new Line("신분당선", "bg-red-600", 강남역, 양재역, 10);
    private static final Line 이호선 = new Line("이호선", "bg-green-600", 교대역, 강남역, 10);
    private static final Line 삼호선 = new Line("삼호선", "bg-orange-600", 교대역, 양재역, 5);

    private static final Section 강남_양재_구간 = new Section(신분당선, 강남역, 양재역, 10);
    private static final Section 교대_강남_구간 = new Section(이호선, 교대역, 강남역, 10);
    private static final Section 교대_남부터미널_구간 = new Section(삼호선, 교대역, 남부터미널역, 3);
    private static final Section 남부터미널_양재_구간 = new Section(삼호선, 남부터미널역, 양재역, 2);

    private List<Station> 전체_역_목록;
    private List<Section> 전체_구간_목록;

    @BeforeEach
    void setUp() {
        전체_역_목록 = Arrays.asList(강남역, 양재역, 교대역, 남부터미널역);
        전체_구간_목록 = Arrays.asList(강남_양재_구간, 교대_강남_구간, 교대_남부터미널_구간, 남부터미널_양재_구간);
    }

    @DisplayName("최단 경로를 조회한다. (목적지로 가는 역을 순서대로 반환한다)")
    @Test
    void findShortestPath() {
        // given
        PathFinder pathFinder = PathFinder.init(전체_역_목록, 전체_구간_목록);

        // when
        Path 최단경로 = pathFinder.findShortestPath(교대역.getId(), 양재역.getId());

        // then
        assertThat(최단경로).isEqualTo(new Path(Arrays.asList(교대역, 남부터미널역, 양재역), 5));
    }

    @DisplayName("연결되어있지 않은 출발역과 도착역으로 최단 경로를 조회한다.")
    @Test
    void findShortestPath_notConnectedSection() {
        // given
        Station 몽촌토성역 = Station.of(5L, "몽촌토성역");
        Station 잠실역 = Station.of(6L, "잠실역");
        Section 몽촌토성역_잠실역_구간 = new Section(new Line("팔호선", "bg-black-600"), 몽촌토성역, 잠실역, 3);

        List<Station> 전체_역_목록 = new ArrayList<>(this.전체_역_목록);
        전체_역_목록.add(몽촌토성역);
        전체_역_목록.add(잠실역);

        List<Section> 전체_구간_목록 = new ArrayList<>(this.전체_구간_목록);
        전체_구간_목록.add(몽촌토성역_잠실역_구간);

        PathFinder pathFinder = PathFinder.init(전체_역_목록, 전체_구간_목록);

        // when & then
        assertThatThrownBy(() -> pathFinder.findShortestPath(교대역.getId(), 몽촌토성역.getId()))
                .isInstanceOf(SectionNotConnectedException.class)
                .hasMessage("구간이 연결되어 있지 않습니다.");
    }

    @DisplayName("존재하지 않는 역으로 최단 경로를 조회한다.")
    @Test
    void findShortestPath_notFoundStation() {
        // given
        Station 몽촌토성역 = Station.of(5L, "몽촌토성역");
        PathFinder pathFinder = PathFinder.init(전체_역_목록, 전체_구간_목록);

        // when & then
        assertThatThrownBy(() -> pathFinder.findShortestPath(교대역.getId(), 몽촌토성역.getId()))
                .isInstanceOf(StationsNotExistException.class)
                .hasMessage("요청한 역들이 존재하지 않습니다.");
    }
}
