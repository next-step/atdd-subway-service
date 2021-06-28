package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class PathFinderTest {
    private Line 삼호선;
    private Line 이호선;
    private Line 신분당선;
    private Station 교대역;
    private Station 남부터미널역;
    private Station 양재역;
    private Station 강남역;
    private Section 교대역_남부터미널역;
    private Section 남부터미널역_양재역;
    private Section 교대역_강남역;
    private Section 강남역_양재역;
    private List<Section> 전체구간;
    private List<Station> 전체역;

    @BeforeEach
    void setUp() {
        삼호선 = new Line();
        이호선 = new Line();
        신분당선 = new Line();

        교대역 = new Station(1L, "교대역");
        남부터미널역 = new Station(2L, "남부터미널역");
        양재역 = new Station(3L, "양재역");
        강남역 = new Station(4L, "강남역");

        교대역_남부터미널역 = new Section(삼호선, 교대역, 남부터미널역, 3);
        남부터미널역_양재역 = new Section(삼호선, 남부터미널역, 양재역, 2);
        교대역_강남역 = new Section(이호선, 교대역, 강남역, 10);
        강남역_양재역 = new Section(신분당선, 강남역, 양재역, 10);
        전체구간 = Arrays.asList(교대역_남부터미널역, 남부터미널역_양재역, 교대역_강남역, 강남역_양재역);
        전체역 = Arrays.asList(교대역, 남부터미널역, 양재역, 강남역);
    }

    @Test
    void findShortestPath() {
        PathFinder pathFinder = PathFinder.of(전체역, 전체구간);
        Path path = pathFinder.getShortestPath(교대역, 양재역);
        assertThat(path.getStations()).isEqualTo(Arrays.asList(교대역, 남부터미널역, 양재역));
        assertThat(path.getDistance()).isEqualTo(5);
    }
}