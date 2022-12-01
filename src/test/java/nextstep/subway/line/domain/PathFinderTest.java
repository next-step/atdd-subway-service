package nextstep.subway.line.domain;

import static nextstep.subway.line.domain.StationFixture.강남역;
import static nextstep.subway.line.domain.StationFixture.교대역;
import static nextstep.subway.line.domain.StationFixture.남부터미널역;
import static nextstep.subway.line.domain.StationFixture.양재;

import java.util.List;
import nextstep.subway.station.domain.Station;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

public class PathFinderTest {

    @Test
    void 최단경로_반환() {
        //given
        Line 이호선 = new Line("이호선", "bg-green-600", 강남역, 교대역, 100);
        Line 삼호선 = new Line("삼호선", "bg-orange-600", 교대역, 남부터미널역, 3);
        삼호선.addSection(남부터미널역, 양재, new Distance(7));
        Line 신분당선 = new Line("신분당선", "bg-red-600", 양재, 강남역, 4);
        PathFinder pathFinder = new PathFinder(이호선, 삼호선, 신분당선);

        //when
        List<Station> shortestPath = pathFinder.shortestPath(강남역, 교대역);

        //then
        Assertions.assertThat(shortestPath)
            .hasSize(4)
            .containsExactly(강남역, 양재, 남부터미널역, 교대역);
    }
}
