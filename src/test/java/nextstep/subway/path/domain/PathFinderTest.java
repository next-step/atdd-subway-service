package nextstep.subway.path.domain;

import nextstep.subway.common.exception.PathDisconnectedException;
import nextstep.subway.common.exception.PathSameException;
import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Section;
import nextstep.subway.line.domain.Sections;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

public class PathFinderTest {

    private static Station 교대역 = null;
    private static Station 강남역 = null;
    private static Station 남부터미널역 = null;
    private static Station 양재역 = null;
    private static Section 첫번째구간 = null;
    private static Section 두번째구간 = null;
    private static Section 세번째구간 = null;
    private static Section 네번째구간 = null;
    private static Sections 전구간 = null;
    private static Station 사당역 = null;
    private static Station 이수역 = null;
    private static Section 다른구간 = null;

    @BeforeEach
    void setUp() {
        교대역 = Station.from("교대역");
        강남역 = Station.from("강남역");
        남부터미널역 = Station.from("남부터미널역");
        양재역 = Station.from("양재역");
        첫번째구간 = Section.of(교대역, 강남역, Distance.of(10));
        두번째구간 = Section.of(강남역, 양재역, Distance.of(10));
        세번째구간 = Section.of(남부터미널역, 양재역, Distance.of(5));
        네번째구간 = Section.of(교대역, 남부터미널역, Distance.of(3));

        사당역 = Station.from("사당역");
        이수역 = Station.from("이수역");
        다른구간 = Section.of(사당역, 이수역, Distance.of(1));

        전구간 = Sections.from(Arrays.asList(첫번째구간, 두번째구간, 세번째구간, 네번째구간, 다른구간));
    }

    /**
     * 교대역    --- *2호선* ---   강남역
     * |                        |
     * *3호선*                   *신분당선*
     * |                        |
     * 남부터미널역  --- *3호선* --- 양재역
     */
    @DisplayName("최적 경로를 찾는다.")
    @Test
    void pathFinder() {
        // when
        PathFinder pathFinder = PathFinder.of(교대역, 양재역, 전구간.getSections());
        PathResponse pathResponse = pathFinder.findShortestPath();

        // then
        assertAll(
                () -> assertThat(pathResponse.getStations()).hasSize(3),
                () -> assertThat(pathResponse.getDistance()).isEqualTo(8.0)
        );
    }

    @DisplayName("경로를 찾는 출발역과 도착역이 같은 경우 에러 처리를 한다.")
    @Test
    void pathFinder_예외1() {
        // when
        PathFinder pathFinder = PathFinder.of(교대역, 교대역, 전구간.getSections());

        // then
        assertThatThrownBy(() -> pathFinder.findShortestPath())
                .isInstanceOf(PathSameException.class)
                .hasMessage("출발역과 도착역이 같은 경우 경로 조회 할 수 없습니다.");
    }

    @DisplayName("출발역과 도착역이 연결이 되어 있지 않은 경우 에러 처리를 한다.")
    @Test
    void pathFinder_예외2() {
        // when
        PathFinder pathFinder = PathFinder.of(교대역, 이수역, 전구간.getSections());
        assertThatThrownBy(() -> pathFinder.findShortestPath())
                .isInstanceOf(PathDisconnectedException.class)
                .hasMessage("요청한 경로는 연결되어 있지 않습니다.");
    }

    @DisplayName("존재하지 않은 출발역이나 도착역을 조회 할 경우 예외처리를 한다.")
    @Test
    void pathFinder_예외3() {
        // when
        PathFinder pathFinder = PathFinder.of(교대역, 이수역, 전구간.getSections());
        assertThatThrownBy(() -> pathFinder.findShortestPath())
                .isInstanceOf(PathDisconnectedException.class)
                .hasMessage("요청한 경로는 연결되어 있지 않습니다.");
    }
}
