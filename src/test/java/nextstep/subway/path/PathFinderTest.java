package nextstep.subway.path;

import nextstep.subway.line.domain.Line;
import nextstep.subway.path.domain.PathFinder;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.junit.jupiter.api.*;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

public class PathFinderTest {
    private Station 강남역, 양재역, 교대역, 남부터미널역, 정자역;
    private Line 신분당선, 이호선, 삼호선;
    private PathFinder pathFinder;


    /**
     * 교대역    --- *2호선(2)* ----   강남역
     * |                            |
     * *3호선(3)*                    *신분당선(10)*
     * |                            |
     * 남부터미널역  --- *3호선(3)* --- 양재역
     */

    @BeforeEach
    public void setUp() {
        강남역 = new Station("강남역");
        양재역 = new Station("양재역");
        교대역 = new Station("교대역");
        남부터미널역 = new Station("남부터미널역");
        정자역 = new Station("정자역");

        신분당선 = new Line("신분당선", "bg-red-600", 강남역, 양재역, 10);
        이호선 = new Line("이호선", "bg-red-600", 교대역, 강남역, 2);
        삼호선 = new Line("삼호선", "bg-red-600", 교대역, 남부터미널역, 3);
        삼호선.addStations(남부터미널역, 양재역, 3);

        pathFinder = pathFinder.of(Arrays.asList(이호선, 삼호선, 신분당선));
    }

    @Test
    public void 최단_경로_찾기() {
        //when
        GraphPath<Station, DefaultWeightedEdge> path = pathFinder.getShortestPath(교대역, 양재역);

        //then
        assertThat(path.getWeight()).isEqualTo(6);
    }

}
