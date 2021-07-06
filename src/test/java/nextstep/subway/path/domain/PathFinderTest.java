package nextstep.subway.path.domain;

import nextstep.subway.exception.Message;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Lines;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static nextstep.subway.TestFixture.*;
import static nextstep.subway.fare.domain.Fare.BASE_FARE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PathFinderTest {

    private PathFinder pathFinder;

    private Line 육호선 = new Line("6호선", "갈색", 연신내역, 응암역, 3, 500);
    private Line 삼호선 = new Line("3호선", "주황색", 연신내역, 불광역, 1, 600);
    private Line 사호선 = new Line("4호선", "파란색", 응암역, 회현역, 1, 1000);

    private List<Line> 모든노선 = new ArrayList<>();

    /*
     *       (3호선,6호선)연신내역ㅡ(1)ㅡ(3호선,6호선)불광역
     *                 \               /
     *                  (3)        (10)
     *                    \        /
     *                  (6호선)응암역ㅡ(1)ㅡ(4호선)회현역
     *
     * */

    @BeforeEach
    void setUp() {
        육호선.addSection(new Section(육호선, 응암역, 불광역, 10));

        모든노선.add(삼호선);
        모든노선.add(육호선);
        모든노선.add(사호선);

        pathFinder = new PathFinder(new Lines(모든노선));
    }

    @DisplayName("다익스트라 알고리즘 이용하여 최단경로 조회")
    @Test
    void 최단경로_조회_성공_다익스트라_알고리즘() {
        //when
        Path shortestPath = pathFinder.getDijkstraShortestPath(불광역, 응암역);

        //then
        assertThat(shortestPath.getStations()).hasSize(3)
                .containsExactly(불광역, 연신내역, 응암역);
        assertThat(shortestPath.getDistance()).isEqualTo(4);
    }

    @DisplayName("출발역과 도착역이 동일한 경우 예외발생")
    @Test
    void 예외상황_출발역_도착역_동일() {
        //when+then
        assertThatThrownBy(() -> pathFinder.getDijkstraShortestPath(불광역, 불광역))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(Message.ERROR_START_AND_END_STATIONS_ARE_SAME.showText());
    }


    @DisplayName("출발역이나 도착역이 등록되어 있지 않은 경우 예외발생")
    @Test
    void 예외상황_출발역_혹은_도착역_등록_안되어있음() {
        Station 강남역 = new Station("강남역");

        //when+then
        assertThatThrownBy(() -> pathFinder.getDijkstraShortestPath(불광역, 강남역))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(Message.ERROR_START_OR_END_STATIONS_NOT_REGISTERED.showText());
    }

    @DisplayName("출발역과 도착역이 연결되어 있지 않은 경우 예외발생")
    @Test
    void 예외상황_출발역_도착역_경로_없음() {
        //Given
        Station 강남역 = new Station("강남역");
        Station 광교역 = new Station("광교역");
        Line 신분당선 = new Line("신분당선", "빨간색", 강남역, 광교역, 10, 300);

        모든노선.add(신분당선);
        PathFinder newPathFinder = new PathFinder(new Lines(모든노선));

        //when+then
        assertThatThrownBy(() -> newPathFinder.getDijkstraShortestPath(불광역, 광교역))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(Message.ERROR_PATH_NOT_FOUND.showText());
    }
}
