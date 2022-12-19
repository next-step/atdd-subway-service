package nextstep.subway.path.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.sun.tools.javac.util.List;
import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("최단 경로 조회 도메인 모델 단위 테스트")
class PathFinderTest {
    private Line 신분당선;
    private Line 일호선;
    private Line 이호선;
    private Line 삼호선;

    private Station 시청역;
    private Station 종각역;
    private Station 강남역;
    private Station 양재역;
    private Station 교대역;
    private Station 남부터미널역;
    
    private PathFinder pathFinder;

    @BeforeEach
    void setUp() {

        시청역 = new Station("시청역");
        종각역 = new Station("종각역");
        강남역 = new Station("강남역");
        양재역 = new Station("양재역");
        교대역 = new Station("교대역");
        남부터미널역 = new Station("남부터미널역");

        신분당선 = new Line("신분당선", "bg-red-600", 강남역, 양재역, 10);
        일호선 = new Line("일호선", "bg-red-600", 시청역, 종각역, 10);
        이호선 = new Line("이호선", "bg-red-600", 교대역, 강남역, 10);
        삼호선 = new Line("삼호선", "bg-red-600", 교대역, 양재역, 5);
        삼호선.addSection(new Section(교대역, 남부터미널역, Distance.from(3)));

        pathFinder = new PathFinder();
    }

    @Test
    void 출발역과_도착역이_같을경우_에러() {
        assertThatThrownBy(() -> pathFinder.findShortestPath(List.of(신분당선, 이호선, 삼호선), 강남역, 강남역))
            .isInstanceOf(IllegalPathException.class)
            .hasMessageContaining("출발역과 도착역이 동일합니다.");
    }

    @Test
    void 출발역과_도착역이_연결되지않을경우_에러() {
        assertThatThrownBy(() -> pathFinder.findShortestPath(List.of(신분당선, 일호선, 이호선, 삼호선), 강남역, 종각역))
            .isInstanceOf(IllegalPathException.class)
            .hasMessageContaining("출발역과 도착역이 연결되어 있지 않습니다.");
    }

    @Test
    void 출발역_또는_도착역이_노선목록에_존재하지않는경우_에러() {
        assertThatThrownBy(() -> pathFinder.findShortestPath(List.of(신분당선), 시청역, 종각역))
            .isInstanceOf(IllegalPathException.class)
            .hasMessageContaining("존재하지 않는 역입니다.");
    }

    @Test
    void 출발역과_도착역이_노선목록에_존재하는경우_최단경로반환() {
        ShortestPath result = pathFinder.findShortestPath(List.of(신분당선, 이호선, 삼호선), 강남역, 남부터미널역);
        assertShortestPathOrderAndDistance(result, List.of(강남역, 양재역, 남부터미널역), 12);
    }

    private void assertShortestPathOrderAndDistance(
        ShortestPath result,
        List<Station> expectedStations,
        int expectedTotalDistance
    ) {
        assertAll(
            () -> assertThat(result.getStations()).containsExactlyElementsOf(expectedStations),
            () -> assertThat(result.getDistance()).isEqualTo(expectedTotalDistance)
        );
    }
}
