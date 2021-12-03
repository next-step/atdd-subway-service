package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Line;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.*;

@DisplayName("최단 경로 Finder")
public class PathFinderTest {

    private Station 강남역;
    private Station 양재역;
    private Station 교대역;
    private Station 남부터미널역;
    private Station 부산역;
    private Line 신분당선;
    private Line 이호선;
    private Line 삼호선;

    @BeforeEach
    public void setUp() {
        강남역 = new Station("강남역");
        양재역 = new Station("양재역");
        교대역 = new Station("교대역");
        남부터미널역 = new Station("남부터미널역");
        부산역 = new Station("부산역");

        신분당선 = new Line("신분당선", "bg-red-600", 강남역, 양재역, 10, 0);
        이호선 = new Line("이호선", "bg-green-600", 교대역, 강남역, 10, 0);
        삼호선 = new Line("삼호선", "bg-orange-600", 교대역, 양재역, 5, 0);

        삼호선.addSection(교대역, 남부터미널역, 3);
    }

    @Test
    @DisplayName("최단 경로를 조회한다.")
    void findShortestPath() {
        // given
        PathFinder pathFinder = new PathFinder(Arrays.asList(신분당선, 이호선, 삼호선));

        // when
        Path path = pathFinder.findShortestPath(강남역, 남부터미널역);

        // then
        assertThat(path).isEqualTo(new Path(Arrays.asList(강남역, 양재역, 남부터미널역), 12, 0));
    }

    @Test
    @DisplayName("동일한 역으로 최단 경로를 조회하면 예외를 발생한다.")
    void findShortestPathThrowException1() {
        // given
        PathFinder pathFinder = new PathFinder(Arrays.asList(신분당선, 이호선, 삼호선));

        // when & then
        assertThatExceptionOfType(PathFindFailedException.class)
                .isThrownBy(() -> pathFinder.findShortestPath(강남역, 강남역))
                .withMessageMatching(PathFinder.SAME_STATION_ERROR);
    }

    @Test
    @DisplayName("연결되어 있지 않은 역으로 최단 경로를 조회하면 예외를 발생한다.")
    void findShortestPathThrowException2() {
        // given
        PathFinder pathFinder = new PathFinder(Arrays.asList(신분당선, 이호선, 삼호선));

        // when & then
        assertThatExceptionOfType(PathFindFailedException.class)
                .isThrownBy(() -> pathFinder.findShortestPath(강남역, 부산역))
                .withMessageMatching(PathFinder.NOT_CONTAINED_STATION_ERROR);
    }
}
