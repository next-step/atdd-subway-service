package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class PathFinderTest {

    private PathFinder pathFinder;

    private Line 육호선 = new Line("6호선", "갈색");
    private Line 삼호선 = new Line("3호선", "주황색");
    private Station 연신내역 = new Station("연신내역");
    private Station 불광역 = new Station("불광역"); //출발
    private Station 응암역 = new Station("응암역"); //도착

    private List<Line> 모든노선 = new ArrayList<>();

    /*
     *       연신내역ㅡ(5)ㅡ불광역
     *          \         /
     *           (5)    (100)
     *             \    /
     *              응암역
     *
     * */

    @BeforeEach
    void setUp() {
        삼호선.addSection(new Section(삼호선, 연신내역, 불광역, 5));
        육호선.addSection(new Section(육호선, 연신내역, 응암역, 5));
        육호선.addSection(new Section(육호선, 응암역, 불광역, 100));

        모든노선.add(삼호선);
        모든노선.add(육호선);

        pathFinder = new PathFinder(모든노선);
    }

    @DisplayName("다익스트라 알고리즘 이용하여 최단경로 조회")
    @Test
    void 최단경로_조회_성공_다익스트라_알고리즘() {
        //when
        Path shortestPath = pathFinder.getDijkstraShortestPath(불광역, 응암역);

        //then
        assertThat(shortestPath.getStations()).hasSize(3)
                .containsExactly(불광역, 연신내역, 응암역);
        assertThat(shortestPath.getDistance()).isEqualTo(10);
    }
}
