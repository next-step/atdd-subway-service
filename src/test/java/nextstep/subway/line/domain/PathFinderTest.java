package nextstep.subway.line.domain;

import static nextstep.subway.line.domain.StationFixture.강남역;
import static nextstep.subway.line.domain.StationFixture.교대역;
import static nextstep.subway.line.domain.StationFixture.남부터미널역;
import static nextstep.subway.line.domain.StationFixture.블루보틀역;
import static nextstep.subway.line.domain.StationFixture.스타벅스역;
import static nextstep.subway.line.domain.StationFixture.양재;

import java.util.List;
import nextstep.subway.line.exception.WrongPathException;
import nextstep.subway.station.domain.Station;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class PathFinderTest {

    private Line 이호선;
    private Line 삼호선;
    private Line 사호선;
    private Line 신분당선;

    @BeforeEach
    void setUp() {
        이호선 = new Line("이호선", "bg-green-600", 강남역, 교대역, 100);
        삼호선 = new Line("삼호선", "bg-orange-600", 교대역, 남부터미널역, 3);
        삼호선.addSection(남부터미널역, 양재, new Distance(7));
        사호선 = new Line("삼호선", "bg-orange-600", 스타벅스역, 블루보틀역, 3);
        신분당선 = new Line("신분당선", "bg-red-600", 양재, 강남역, 4);
    }

    @Test
    void 최단경로_반환() {
        //given
        PathFinder pathFinder = new PathFinder(이호선, 삼호선, 신분당선);

        //when
        List<Station> shortestPath = pathFinder.shortestPath(강남역, 교대역);

        //then
        Assertions.assertThat(shortestPath)
            .hasSize(4)
            .containsExactly(강남역, 양재, 남부터미널역, 교대역);
    }

    @Test
    void 최단경로_조회시_출발역과_도착역이_같으면_에러() {
        //given
        PathFinder pathFinder = new PathFinder(이호선);

        //when & then
        Assertions.assertThatThrownBy(() -> pathFinder.shortestPath(강남역, 강남역))
            .isInstanceOf(WrongPathException.class)
            .hasMessage("출발역과 도착역이 동일합니다.");
    }

    @Test
    void 출발역과_도착역이_연결되어있지_않은경우_에러_발생() {
        //given
        PathFinder pathFinder = new PathFinder(이호선, 사호선);

        //when & then
        Assertions.assertThatThrownBy(() -> pathFinder.shortestPath(강남역, 블루보틀역))
            .isInstanceOf(WrongPathException.class)
            .hasMessage("출발역과 도착역이 연결되어 있지 않습니다.");
    }

    @Test
    void 출발역과_도착역이_없는경우_에러_발생() {
        //given
        PathFinder pathFinder = new PathFinder(이호선);

        //when & then
        Assertions.assertThatThrownBy(() -> pathFinder.shortestPath(스타벅스역, 블루보틀역))
            .isInstanceOf(WrongPathException.class)
            .hasMessage("존재하지 않는 역입니다.");
    }
}
