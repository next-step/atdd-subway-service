package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Line;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

class PathFinderTest {

    private Line 신분당선;
    private Line 삼호선;
    private Line 이호선;
    private Station 강남역;
    private Station 양재역;
    private Station 남부터미널역;
    private Station 서초역;
    private Station 교대역;

    @BeforeEach
    public void setUp() {
        강남역 = new Station("강남역");
        양재역 = new Station("양재역");
        남부터미널역 = new Station("남부터미널역");
        서초역 = new Station("서초역");
        교대역 = new Station("교대역");
        신분당선 = new Line("신분당선", "red", 0, 강남역, 양재역, new Distance(10));
        삼호선 = new Line("신분당선", "red", 500, 양재역, 남부터미널역, new Distance(7));
        이호선 = new Line("신분당선", "red", 900, 서초역, 교대역, new Distance(15));
    }

    @Test
    void 최단거리를_구할_수_있다() {
        PathFinder pathFinder = new PathFinder(Arrays.asList(신분당선, 삼호선));

        Path result = pathFinder.findPath(강남역, 남부터미널역);

        assertThat(result.getStations()).containsExactly(강남역, 양재역, 남부터미널역);
        assertThat(result.getDistance()).isEqualTo(17);
    }

    @Test
    void 출발역과_도착역이_동일하면_최단거리를_구할_수_없다() {
        PathFinder pathFinder = new PathFinder(Arrays.asList(신분당선, 삼호선));

        assertThatIllegalArgumentException().isThrownBy(() -> pathFinder.findPath(강남역, 강남역));
    }

    @Test
    void 출발역과_도착역이_연결이_되어_있지_않으면_최단거리를_구할_수_없다() {
        PathFinder pathFinder = new PathFinder(Arrays.asList(신분당선, 삼호선));

        assertThatIllegalArgumentException().isThrownBy(() -> pathFinder.findPath(강남역, 서초역));
    }

}