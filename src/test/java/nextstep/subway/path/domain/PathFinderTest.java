package nextstep.subway.path.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Arrays;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class PathFinderTest {

    private Line 신분당선;
    private Line 이호선;
    private Line 삼호선;
    private Line 오호선;
    private Station 강남역;
    private Station 양재역;
    private Station 교대역;
    private Station 남부터미널역;
    private Station 마곡역;
    private Station 마포역;

    /**
     * 교대역    --- *2호선* ---   강남역
     * |                        |
     * *3호선*                   *신분당선*
     * |                        |
     * 남부터미널역  --- *3호선* ---   양재
     *
     * 마곡역 --- *5호선* --- 마포역
     */
    @BeforeEach
    public void setUp() {
        강남역 = new Station("강남역");
        양재역 = new Station("양재역");
        교대역 = new Station("교대역");
        남부터미널역 = new Station("남부터미널역");
        마곡역 = new Station("마곡역");
        마포역 = new Station("마포역");

        신분당선 = new Line("신분당선", "bg-red-600", 강남역, 양재역, 10);
        이호선 = new Line("이호선", "bg-red-600", 교대역, 강남역, 10);
        삼호선 = new Line("삼호선", "bg-red-600", 교대역, 양재역, 5);
        오호선 = new Line("오호선", "bg-red-600", 마곡역, 마포역, 5);

        삼호선.addSection(Section.of(삼호선, 교대역, 남부터미널역, 3));
    }

    @DisplayName("출발역과 도착역의 최단거리를 조회한다.")
    @Test
    void findShortestPath() {
        PathFinder pathFinder = PathFinder.from(Arrays.asList(신분당선, 이호선, 삼호선));

        Path path = pathFinder.findShortestPath(강남역, 남부터미널역);

        assertThat(path.getDistance().value()).isEqualTo(12);
    }

    @DisplayName("출발역과 도착역이 동일한 경우 에러가 발생된다.")
    @Test
    void validateSameStationException() {
        PathFinder pathFinder = PathFinder.from(Arrays.asList(신분당선, 이호선, 삼호선));

        assertThatThrownBy(() -> pathFinder.findShortestPath(강남역, 강남역))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("출발역과 도착역이 연결이 되어 있지 않은 경우 에러가 발생된다.")
    @Test
    void validateNotConnectException() {
        PathFinder pathFinder = PathFinder.from(Arrays.asList(신분당선, 이호선, 삼호선, 오호선));

        assertThatThrownBy(() -> pathFinder.findShortestPath(강남역, 마곡역))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("존재하지 않은 출발역이나 도착역을 조회할 경우 에러가 발생된다.")
    @Test
    void validateNotExistStationException() {
        PathFinder pathFinder = PathFinder.from(Arrays.asList(신분당선, 이호선, 삼호선));

        assertThatThrownBy(() -> pathFinder.findShortestPath(마곡역, 마포역))
            .isInstanceOf(IllegalArgumentException.class);
    }
}
