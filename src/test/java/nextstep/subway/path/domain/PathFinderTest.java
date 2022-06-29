package nextstep.subway.path.domain;

import nextstep.subway.exception.NoSearchStationException;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

public class PathFinderTest {
    private Line 신분당선;
    private Line 이호선;
    private Line 삼호선;
    private Line 경강선;
    private List<Line> lines;
    private Station 강남역;
    private Station 양재역;
    private Station 교대역;
    private Station 남부터미널역;
    private Station 세종대왕릉역;
    private Station 여주역;
    private final PathFinder pathFinder = new PathFinder();

    /**
     * 교대역    --- *2호선* ---   강남역      세종대왕릉역
     * |                          |            |
     * *3호선*                   *신분당선*     *경강선*
     * |                          |            |
     * 남부터미널역  --- *3호선* ---  양재        여주역
     */
    @BeforeEach
    public void setUp() {
        강남역 = new Station("강남역");
        양재역 = new Station("양재역");
        교대역 = new Station("교대역");
        남부터미널역 = new Station("남부터미널역");
        세종대왕릉역 = new Station("세종대왕릉역");
        여주역 = new Station("여주역");

        신분당선 = new Line("신분당선", "bg-red-600", 강남역, 양재역, 10);
        이호선 = new Line("이호선", "bg-red-600", 교대역, 강남역, 6);
        삼호선 = new Line("삼호선", "bg-red-600", 교대역, 양재역, 4);
        경강선 = new Line("경강선", "bg-red-600", 세종대왕릉역, 여주역, 12);
        lines = Arrays.asList(신분당선, 이호선, 삼호선, 경강선);

        삼호선.addSection(new Section(삼호선, 교대역, 남부터미널역, 3));
    }

    @DisplayName("두 역의 최단 거리 경로를 조회한다.")
    @Test
    void findPath() {
        // when
        Path path = pathFinder.findPath(lines, 교대역, 양재역);

        // then
        assertAll(
                () -> assertThat(path.getStations()).containsExactly(교대역, 남부터미널역, 양재역),
                () -> assertThat(path.getDistance().getDistance()).isEqualTo(4)
        );
    }

    @DisplayName("최단 거리 경로 조회 실패 (출발역과 도착역이 같은 경우)")
    @Test
    void findPathFail_equalStation() {
        // then
        assertThatThrownBy(() -> {
            pathFinder.findPath(lines, 교대역, 교대역);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("최단 거리 경로 조회 실패 (출발역과 도착역이 연결이 되어 있지 않은 경우)")
    @Test
    void findPathFail_notConnected() {
        // then
        assertThatThrownBy(() -> {
            pathFinder.findPath(lines, 교대역, 세종대왕릉역);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("최단 거리 경로 조회 실패 (존재하지 않은 출발역이나 도착역을 조회 할 경우)")
    @Test
    void findPathFail_notExist() {
        // then
        assertThatThrownBy(() -> {
            pathFinder.findPath(lines, 교대역, new Station());
        }).isInstanceOf(NoSearchStationException.class);
    }
}
