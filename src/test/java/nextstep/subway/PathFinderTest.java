package nextstep.subway;

import nextstep.subway.line.domain.Line;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class PathFinderTest {

    Station 강남역;
    Station 양재역;
    Station 교대역;
    Station 남부터미널역;
    Line 신분당선;
    Line 이호선;
    Line 삼호선;

    /**
     * 교대역     --- *2호선* ---  강남역
     *  |                         |
     * *3호선*                  *신분당선*
     *  |                         |
     * 남부터미널역 --- *3호선* ---  양재
     */
    @BeforeEach
    void init() {
        강남역 = new Station("강남역");
        양재역 = new Station("양재역");
        교대역 = new Station("교대역");
        남부터미널역 = new Station("남부터미널역");

        신분당선 = new Line("신분당선", "bg-red-600", 강남역, 양재역, 10);
        이호선 = new Line("이호선", "bg-red-600", 교대역, 강남역, 10);
        삼호선 = new Line("삼호선", "bg-red-600", 교대역, 양재역, 5);

        삼호선.addSection(교대역, 남부터미널역, 3);
    }

    @DisplayName("출발 역부터 도착 역까지 최단 경로를 조회한다.")
    @Test
    void 경로_조회() {
        // given
        PathFinder pathFinder = new PathFinder(Arrays.asList(신분당선, 이호선, 삼호선));

        // when
        List<Station> route = pathFinder.findRoute(강남역, 남부터미널역);

        // then
        assertThat(route).containsExactly(강남역, 양재역, 남부터미널역);
    }
}
