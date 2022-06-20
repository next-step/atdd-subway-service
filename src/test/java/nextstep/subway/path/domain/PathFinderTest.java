package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class PathFinderTest {
    private Station 강남역;
    private Station 양재역;
    private Station 남부터미널역;
    private Station 교대역;

    private Line 신분당선;
    private Line 삼호선;
    private Line 이호선;

    private Section 강남역_양재역;
    private Section 교대역_남부터미널역;
    private Section 남부터미널역_양재역;
    private Section 교대역_강남역;

    private List<Station> 모든역;
    private List<Section> 모든구간;

    @BeforeEach
    void setUp() {
        강남역 = Station.of(1L, "강남역");
        양재역 = Station.of(2L, "양재역");
        남부터미널역 = Station.of(3L,"남부터미널역");
        교대역 = Station.of(4L,"교대역");

        신분당선 = new Line("신분당선", "bg-red-600");
        삼호선 = new Line("3호선", "bg-orange-600");
        이호선 = new Line("2호선", "bg-green-600");

        강남역_양재역 = new Section(신분당선, 강남역, 양재역, 3000);
        교대역_남부터미널역 = new Section(삼호선, 교대역, 남부터미널역, 2000);
        남부터미널역_양재역 = new Section(삼호선, 남부터미널역, 양재역, 2500);
        교대역_강남역 = new Section(이호선, 교대역, 강남역, 3000);

        모든역 = Arrays.asList(강남역, 양재역, 남부터미널역, 교대역);
        모든구간 = Arrays.asList(강남역_양재역, 교대역_남부터미널역, 남부터미널역_양재역, 교대역_강남역);
    }

    @Test
    @DisplayName("최단경로 조회 시 출발역부터 도착지까지의 역을 순서대로 셋팅하고 총 거리를 계산한다.")
    void findShortestPath() {
        // given
        PathFinder pathFinder = PathFinder.init(모든역, 모든구간);

        // when
        Path 최단경로 = pathFinder.findShortestPath(양재역.getId(), 교대역.getId());

        // then
        assertThat(최단경로)
                .isEqualTo(Path.of(Arrays.asList(양재역, 남부터미널역, 교대역), 4500));
    }
}
