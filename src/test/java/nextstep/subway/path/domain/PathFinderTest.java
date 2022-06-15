package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Lines;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("최단 경로 찾기")
class PathFinderTest {
    private Lines lines;
    private PathFinder pathFinder;
    private Station 강남역;
    private Station 교대역;
    private Station 수진역;
    private Station 사당역;

    @BeforeEach
    void setUp() {
        강남역 = new Station( "강남역");
        교대역 = new Station( "교대역");
        사당역 = new Station( "사당역");
        수진역 = new Station( "수진역");
        Station 양재역 = new Station( "양재역");
        Station 남부터미널역 = new Station( "남부터미널역");
        Station 이수역 = new Station( "이수역");

        Line 신분당선 = new Line("신분당선", "red", 강남역, 양재역, 4);
        Line 삼호선 = new Line("삼호선", "orange", 교대역, 남부터미널역, 3);
        Line 이호선 = new Line("이호선", "green", 교대역, 강남역, 10);
        Line 사호선 = new Line("사호선", "blue", 사당역, 이수역, 10);

        삼호선.addSection(new Section(남부터미널역, 양재역, 2));

        lines = new Lines(Arrays.asList(신분당선, 삼호선, 이호선, 사호선));
        pathFinder = new PathFinder();
    }

    @Test
    void 최단_경로_목록을_반환한다() {
        // when
        ShortestPath result = pathFinder.findShortestPath(lines, 강남역, 교대역);

        // then
        assertAll(
                () -> assertThat(result.getPath()).hasSize(4),
                () -> assertThat(result.getDistance()).isEqualTo(9)
        );
    }
    
    @Test
    void 출발역과_도착역이_같을_경우_최단경로를_구할_수_없다() {
        // when & then
        assertThatThrownBy(() ->
            pathFinder.findShortestPath(lines, 강남역, 강남역)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 노선에_지하철_역이_포함되지_않은_경우_최단경로를_구할_수_없다() {
        // when & then
        assertThatThrownBy(() ->
                pathFinder.findShortestPath(lines, 강남역, 수진역)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 출발역과_도착역이_연결되어_있지_않으면_최단경로를_구할_수_없다() {
        // when & then
        assertThatThrownBy(() ->
                pathFinder.findShortestPath(lines, 강남역, 사당역)
        ).isInstanceOf(RuntimeException.class);
    }
}
