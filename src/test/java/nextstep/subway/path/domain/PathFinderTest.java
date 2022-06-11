package nextstep.subway.path.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.Arrays;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PathFinderTest {

    private Line 신분당선;
    private Line 이호선;
    private Line 삼호선;
    private Station 강남역;
    private Station 양재역;
    private Station 교대역;
    private Station 남부터미널역;

    /**
     * 교대역      --- *2호선* ---   강남역
     * |                            |
     * *3호선*                    *신분당선*
     * |                            |
     * 남부터미널역  --- *3호선* ---   양재
     */
    @BeforeEach
    void before() {
        강남역 = new Station(1L, "강남역");
        양재역 = new Station(2L,"양재역");
        교대역 = new Station(3L,"교대역");
        남부터미널역 = new Station(4L,"남부터미널역");

        신분당선 = new Line("신분당선","red", 강남역, 양재역, 10);
        이호선 = new Line("이호선","green", 교대역, 강남역, 10);
        삼호선 = new Line("삼호선","orange", 교대역, 양재역, 5);

        Section 삼호선_새로운구간 = new Section(삼호선, 교대역, 남부터미널역, 3);
        삼호선.addSection(삼호선_새로운구간);
    }

    @Test
    void findShortestTest() {

        PathFinder finder = new PathFinder(Arrays.asList(신분당선, 이호선, 삼호선));
        Path path = finder.findShortest(양재역, 교대역);

        assertAll(
                () -> assertThat(path.getShortestPath()).containsExactly(양재역,남부터미널역,교대역),
                () -> assertThat(path.getShortestDistance()).isEqualTo(5)
        );
    }

}
