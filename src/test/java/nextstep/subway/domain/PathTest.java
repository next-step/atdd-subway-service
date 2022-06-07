package nextstep.subway.domain;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Path;
import nextstep.subway.line.domain.PathResult;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

public class PathTest {

    private Station 교대역 = new Station("교대역");
    private Station 강남역 = new Station("강남역");
    private Station 남부터미널역 = new Station("남부터미널역");
    private Station 양재역 = new Station("양재역");

    private Line 삼호선;
    private Line 이호선;
    private Line 신분당선;

    /**
     *    (*3호선*)
     *      교대역    --- 10 ------  강남역 (*2호선*)
     *       |                      |
     *       3                      10
     *       |                      |
     *     남부터미널역 ---  2 ------  양재 (*3호선*)
     *                          (*신분당선*)
     */
    @BeforeEach
    void setup() {
        이호선 = new Line("2호선", "green", 교대역, 강남역, 10);
        신분당선 = new Line("신분당선", "red", 강남역, 양재역, 10);
        삼호선 = new Line("삼호선", "orange", 교대역, 양재역, 5);
        삼호선.addSection(new Section(교대역, 남부터미널역, 3));
    }

    @DisplayName("교대역 -> 양재역 최단 경로 길이는 5이다.")
    @Test
    void findShortest() {
        PathResult result = Path.of().findShortest(Arrays.asList(이호선, 신분당선, 삼호선), 교대역, 양재역);
        assertThat(result.getStations().size()).isEqualTo(3);
        assertThat(result.getDistance()).isEqualTo(5);
    }
}
