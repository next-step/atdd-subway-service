package nextstep.subway.path.unit;

import nextstep.subway.exception.InvalidRequestException;
import nextstep.subway.line.domain.Line;
import nextstep.subway.path.domain.Path;
import nextstep.subway.path.domain.PathFinder;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("지하철 경로 기능")
public class PathFinderTest {

    private Line 신분당선;
    private Line 이호선;
    private Line 삼호선;
    private Line 이어지지않는호선;
    private Station 강남역;
    private Station 양재역;
    private Station 교대역;
    private Station 남부터미널역;
    private Station 이어지지않는역_1;
    private Station 이어지지않는역_2;

    /**
     * 교대역    --- *2호선* ---   강남역
     * |                        |
     * *3호선*                   *신분당선*
     * |                        |
     * 남부터미널역  --- *3호선* ---   양재
     */
    @BeforeEach
    public void setUp() {
        강남역 = new Station("강남역");
        양재역 = new Station("양재역");
        교대역 = new Station("교대역");
        남부터미널역 = new Station("남부터미널역");
        이어지지않는역_1 = new Station("이어지지않는역");
        이어지지않는역_2 = new Station("이어지지않는역_2");

        신분당선 = new Line("신분당선", "빨간색", 강남역, 양재역, 10);
        이호선 = new Line("이호선", "빨간색", 교대역, 강남역, 10);
        삼호선 = new Line("삼호선", "빨간색", 교대역, 양재역, 5);
        이어지지않는호선 = new Line("이어지지않는호선", "빨간색", 이어지지않는역_1, 이어지지않는역_2, 5);

        삼호선.addStation(교대역, 남부터미널역, 3);
    }

    @Test
    @DisplayName("최소경로 조회")
    void getFastPath() {
        PathFinder pathFinder = new PathFinder();
        Path path = pathFinder.findFastPaths(Arrays.asList(신분당선, 이호선, 삼호선), 교대역, 양재역);
        assertThat(path.getDistance()).isEqualTo(5);
    }

    @Test
    @DisplayName("이어지지 않는 역의 최소경로 조회")
    void getFastPathNotLinked() {
        PathFinder pathFinder = new PathFinder();
        assertThrows(InvalidRequestException.class, ()
                -> pathFinder.findFastPaths(Arrays.asList(신분당선, 이호선, 삼호선, 이어지지않는호선), 이어지지않는역_1, 양재역));
    }

}
