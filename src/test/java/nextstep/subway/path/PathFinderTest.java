package nextstep.subway.path;

import nextstep.subway.line.domain.Line;
import nextstep.subway.path.domain.Path;
import nextstep.subway.path.domain.PathFinder;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

public class PathFinderTest {

    private Line 신분당선;
    private Line 이호선;
    private Line 삼호선;
    private Line 구호선;
    private Station 강남역;
    private Station 양재역;
    private Station 교대역;
    private Station 남부터미널역;
    private Station 동작역;
    private Station 석촌역;


    /**
     * 교대역 -------- 2호선(10) ------ 강남역
     * ㅣ                               ㅣ
     * ㅣ                               ㅣ
     * 3호선(3)                      신분당선(10)
     * ㅣ                               ㅣ
     * ㅣ                               ㅣ
     * 남부터미널역 ------3호선(2) ------- 양재
     * <p>
     * 동작역 --------- 9호선(13) ------ 석촌역
     */

    @BeforeEach
    public void setUp() {
        강남역 = new Station("강남역");
        양재역 = new Station("양재역");
        교대역 = new Station("교대역");
        남부터미널역 = new Station("남부터미널역");
        동작역 = new Station("동작역");
        석촌역 = new Station("석촌역");

        신분당선 = new Line("신분당선", "bg-red-600", 강남역, 양재역, 10);
        이호선 = new Line("이호선", "bg-red-600", 교대역, 강남역, 10);
        삼호선 = new Line("삼호선", "bg-red-600", 교대역, 양재역, 5);
        구호선 = new Line("구호선", "bg-red-600", 동작역, 석촌역, 13);
    }

    @DisplayName("출발역과 도착역 사이 최단 경로 조회")
    @Test
    public void 최단경로조회() {
        // given
        PathFinder pathFinder = new PathFinder(Arrays.asList(삼호선, 이호선));
        // when
        Path path = pathFinder.findShortPath(교대역, 양재역);
        // then
        assertAll(
                () -> assertThat(path.getStations()).hasSize(2),
                () -> assertThat(path.getDistance()).isEqualTo(5)
        );
    }

    @DisplayName("출발역과 도착역이 같은 경우 예외 발생")
    @Test
    public void 최단경로조회_예외발생1() {
        // given
        PathFinder pathFinder = new PathFinder(Arrays.asList(삼호선, 이호선));
        // when && then
        assertThatThrownBy(
                () -> pathFinder.findShortPath(양재역, 양재역))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("출발역과 도착역이 같을 경우 최단 거리를 조회할 수 없습니다.");
    }

}
