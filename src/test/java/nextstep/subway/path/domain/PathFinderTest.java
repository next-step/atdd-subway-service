package nextstep.subway.path.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.Arrays;
import nextstep.subway.line.domain.Line;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
     * 교대역      ----- *2호선(10)* -----   강남역
     * |                                   |
     * |                                   |
     * *3호선(3)*                       *신분당선(10)*
     * |                                   |
     * |                                   |
     * 남부터미널역 ----- *3호선(2)* -----     양재
     *
     * 동작역     ----- *9호선(13)* -----    석촌역
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
        구호선 = new Line("삼호선", "bg-red-600", 동작역, 석촌역, 13);
    }

    @Test
    void 출발역과_도착역_사이의_최단_경로_조회() {
        PathFinder pathFinder = PathFinder.from(Arrays.asList(삼호선, 이호선));
        Path path = pathFinder.findShortestPath(교대역, 양재역);

        assertAll(
                () -> assertThat(path.getStations()).hasSize(2),
                () -> assertThat(path.getDistance()).isEqualTo(5)
        );
    }

    @Test
    void 출발역과_도착역이_같은_경우_예외_발생() {
        PathFinder pathFinder = PathFinder.from(Arrays.asList(삼호선, 이호선));

        assertThatThrownBy(() -> pathFinder.findShortestPath(양재역, 양재역))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    void 출발역과_도착역이_연결이_되어_있지_않는_경우_예외_발생() {
        PathFinder pathFinder = PathFinder.from(Arrays.asList(삼호선, 이호선));

        assertThatThrownBy(() -> pathFinder.findShortestPath(교대역, 동작역))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    void 존재하지_않는_출발역이나_도착역을_조회_할_경우_예외_발생() {
        PathFinder pathFinder = PathFinder.from(Arrays.asList(삼호선, 이호선));

        assertThatThrownBy(() -> pathFinder.findShortestPath(양재역, 남부터미널역))
                .isInstanceOf(RuntimeException.class);
    }

}
