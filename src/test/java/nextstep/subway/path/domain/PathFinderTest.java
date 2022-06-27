package nextstep.subway.path.domain;

import nextstep.subway.exception.IllegalArgumentException;
import nextstep.subway.exception.NoSuchElementFoundException;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Lines;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class PathFinderTest {
    private Line 신분당선;
    private Line 이호선;
    private Line 삼호선;
    private Line 오호선;

    private Station 강남역;
    private Station 양재역;
    private Station 교대역;
    private Station 남부터미널역;
    private Station 군자역;
    private Station 미사역;

    private Lines lines;

    private PathFinder pathFinder;


    /**
     * 교대역    --- *2호선* (7) ---   강남역
     * |                              |
     * *3호선*                      *신분당선*
     * (3)                           (10)
     * |                              |
     * 남부터미널역  --- *3호선* (2)---   양재
     *
     */
    @BeforeEach
    public void setUp() {
        강남역 = new Station("강남역");
        양재역 = new Station("양재역");
        교대역 = new Station("교대역");
        남부터미널역 = new Station("남부터미널역");
        군자역 = new Station("군자역");
        미사역 = new Station("미사역");

        신분당선 = new Line("신분당선", "bg-red-600", 강남역, 양재역, 10);
        이호선 = new Line("이호선", "bg-green-600", 교대역, 강남역, 7);
        삼호선 = new Line("삼호선", "bg-orange-600", 교대역, 양재역, 5);
        오호선 = new Line("오호선", "bg-purple-600",군자역, 미사역, 12);

        삼호선.addSection(교대역, 남부터미널역, 3);

        lines = new Lines(Arrays.asList(신분당선, 이호선, 삼호선, 오호선));
        pathFinder = new PathFinder(lines);
    }

    @Test
    void GraphPath_확인() {
        GraphPath path = pathFinder.getGraphPath(교대역, 양재역);

        assertAll(
                () -> assertThat(path.getStartVertex()).isEqualTo(교대역),
                () -> assertThat(path.getEndVertex()).isEqualTo(양재역),
                () -> assertThat(path.getVertexList()).containsAll(Arrays.asList(교대역, 남부터미널역, 양재역)),
                () -> assertThat(path.getWeight()).isEqualTo(5L)
        );
    }

}
