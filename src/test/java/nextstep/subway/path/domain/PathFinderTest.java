package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Lines;
import nextstep.subway.path.exception.NotConnectedException;
import nextstep.subway.path.exception.SameSourceAndTargetException;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PathFinderTest {
    private Station 잠실역;
    private Station 교대역;
    private Station 강남역;
    private Station 양재역;
    private Station 남부터미널역;
    private Line 이호선;
    private Line 신분당선;
    private Line 삼호선;
    private PathFinder pathFinder;

    /**
     * 교대역    --- *2호선* (10) ---   강남역
     * |                        |
     * *3호선* (5)                   *신분당선* (10)
     * |                        |
     * 남부터미널역  --- *3호선* (3) ---   양재역
     */
    @BeforeEach
    void setUp() {
        잠실역 = new Station("잠실역");

        교대역 = new Station("교대역");
        강남역 = new Station("강남역");
        양재역 = new Station("양재역");
        남부터미널역 = new Station("남부터미널역");

        신분당선 = new Line("신분당선", "bg-red-600");
        신분당선.addSection(강남역, 양재역, 10);

        이호선 = new Line("2호선", "bg-green-600");
        이호선.addSection(교대역, 강남역, 10);

        삼호선 = new Line("3호선", "bg-orange-600");
        삼호선.addSection(교대역, 남부터미널역, 5);
        삼호선.addSection(남부터미널역, 양재역, 3);

        pathFinder = new PathFinder();
    }

    @Test
    void getShortestDistance() {
        Path path = pathFinder.getShortestDistance(new Lines(Arrays.asList(이호선, 삼호선, 신분당선)), 교대역, 양재역);

        assertThat(path.getDistance()).isEqualTo(8);
    }

    @Test
    void getShortestDistanceWithSameStations() {
        assertThatThrownBy(() -> {
            pathFinder.getShortestDistance(new Lines(Arrays.asList(이호선, 삼호선, 신분당선)), 교대역, 교대역);
        }).isInstanceOf(SameSourceAndTargetException.class);
    }

    @Test
    void getShortestDistanceWithNotExistenceStationsInSections() {
        assertThatThrownBy(() -> {
            pathFinder.getShortestDistance(new Lines(Arrays.asList(이호선, 삼호선, 신분당선)), 교대역, 잠실역);
        }).isInstanceOf(NotConnectedException.class);
    }
}
