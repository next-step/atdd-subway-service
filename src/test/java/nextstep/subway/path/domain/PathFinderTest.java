package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.line.exception.InvalidPathException;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;
import org.assertj.core.api.Assertions;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PathFinderTest {

    private Station 강남역;
    private Station 양재역;
    private Station 교대역;
    private Station 남부터미널역;
    private Line 신분당선;
    private Line 이호선;
    private Line 삼호선;

    @BeforeEach
    void setup() {
        강남역 = new Station("강남역");
        양재역 = new Station("양재역");
        교대역 = new Station("교대역");
        남부터미널역 = new Station("남부터미널역");

        신분당선 = new Line("신분당선", "color");
        이호선 = new Line("2호선", "color");
        삼호선 = new Line("3호선", "color");
    }

    /**
     * <교대역>  ---   *2호선*(5m)   ---   강남역
     * |                                 |
     * *3호선* (5m)                    *신분당선* (10m)
     * |                                 |
     * 남부터미널역   --- *3호선*(5m) ---   <양재역>
     */
    @Test
    void findShortestPath() {
        신분당선.addSection(new Section(신분당선, 강남역, 양재역, 10));
        이호선.addSection(new Section(이호선, 교대역, 강남역, 5));
        삼호선.addSection(new Section(삼호선, 교대역, 남부터미널역, 5));
        삼호선.addSection(new Section(삼호선, 남부터미널역, 양재역, 5));

        PathFinder pathFinder = new PathFinder(Lists.newArrayList(신분당선, 이호선, 삼호선));
        PathResponse response = pathFinder.find(교대역, 양재역);

        Assertions.assertThat(response.getDistance()).isEqualTo(10);
        Assertions.assertThat(response.getStations())
                .extracting(StationResponse::getName)
                .containsExactly(
                        교대역.getName(),
                        남부터미널역.getName(),
                        양재역.getName());
    }

    /**
     * <교대역>  ---   *2호선*(5m)   ---   강남역
     * |                                 |
     * *3호선* (5m)                    *신분당선* (1m)
     * |                                 |
     * 남부터미널역   --- *3호선*(5m) ---   <양재역>
     */
    @Test
    void findShortestPath2() {
        신분당선.addSection(new Section(신분당선, 강남역, 양재역, 1));
        이호선.addSection(new Section(이호선, 교대역, 강남역, 5));
        삼호선.addSection(new Section(삼호선, 교대역, 남부터미널역, 5));
        삼호선.addSection(new Section(삼호선, 남부터미널역, 양재역, 5));

        PathFinder pathFinder = new PathFinder(Lists.newArrayList(신분당선, 이호선, 삼호선));
        PathResponse response = pathFinder.find(교대역, 양재역);

        Assertions.assertThat(response.getDistance()).isEqualTo(6);
        Assertions.assertThat(response.getStations())
                .extracting(StationResponse::getName)
                .containsExactly(
                        교대역.getName(),
                        강남역.getName(),
                        양재역.getName());
    }

    @Test
    @DisplayName("출발역과 도착역이 같은 경우")
    void sourceAndTargetEqual() {
        신분당선.addSection(new Section(신분당선, 강남역, 양재역, 1));
        이호선.addSection(new Section(이호선, 교대역, 강남역, 5));

        PathFinder pathFinder = new PathFinder(Lists.newArrayList(신분당선, 이호선));
        assertThatThrownBy(() -> pathFinder.find(교대역, 교대역))
                .isInstanceOf(InvalidPathException.class);
    }

    @Test
    @DisplayName("출발역과 도착역이 연결이 되어 있지 않은 경우")
    void sourceAndTargetNotConnected() {
        신분당선.addSection(new Section(신분당선, 강남역, 양재역, 1));
        삼호선.addSection(new Section(삼호선, 교대역, 남부터미널역, 5));

        PathFinder pathFinder = new PathFinder(Lists.newArrayList(신분당선, 삼호선));
        assertThatThrownBy(() -> pathFinder.find(강남역, 남부터미널역))
                .isInstanceOf(InvalidPathException.class);
    }

    @Test
    @DisplayName("존재하지 않은 출발역을 조회 할 경우")
    void sourceStationNotExists() {
        신분당선.addSection(new Section(신분당선, 강남역, 양재역, 1));

        PathFinder pathFinder = new PathFinder(Lists.newArrayList(신분당선));
        assertThatThrownBy(() -> pathFinder.find(강남역, 남부터미널역))
                .isInstanceOf(InvalidPathException.class);
    }

    @Test
    @DisplayName("존재하지 않은 도착역을 조회 할 경우")
    void targetStationNotExists() {
        신분당선.addSection(new Section(신분당선, 강남역, 양재역, 1));

        PathFinder pathFinder = new PathFinder(Lists.newArrayList(신분당선));
        assertThatThrownBy(() -> pathFinder.find(교대역, 양재역))
                .isInstanceOf(InvalidPathException.class);
    }
}
