package nextstep.subway.path.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.Arrays;
import nextstep.subway.line.domain.Line;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PathFinderTest {

    private Station 고속터미널역;
    private Station 교대역;
    private Station 강남역;
    private Station 신논현역;
    private Line 신분당선;
    private Line 이호선;
    private Line 삼호선;
    private Line 구호선;

    /**
     * 고속터미널역 --- *9호선*(4) --- 신논현역
     * |                            |
     * *3호선(3)*                 *신분당선*(1)
     * |                            |
     * 교대역   --- *2호선*(3) ---   강남역
     */
    @BeforeEach
    public void setUp() {
        고속터미널역 = new Station("고속터미널역");
        교대역 = new Station("교대역");
        강남역 = new Station("강남역");
        신논현역 = new Station("신논현역");

        신분당선 = new Line("신분당선", "red", 신논현역, 강남역, 1);
        이호선 = new Line("이호선", "green", 교대역, 강남역, 3);
        삼호선 = new Line("삼호선", "orange", 고속터미널역, 교대역, 3);
        구호선 = new Line("구호선", "gold", 고속터미널역, 신논현역, 4);
    }

    @Test
    @DisplayName("노선 정보를 받아 주어진 역 간 최단 경로를 계산한다")
    void getShortestPath() {
        // given
        PathFinder pathFinder = new PathFinder(Arrays.asList(신분당선, 이호선, 삼호선, 구호선));

        // when
        GraphPath<Station, DefaultWeightedEdge> shortestPath = pathFinder.getShortestPath(고속터미널역, 강남역);

        // then
        assertAll(
                () -> assertThat(shortestPath.getVertexList()).containsExactly(고속터미널역, 신논현역, 강남역),
                () -> assertThat(shortestPath.getWeight()).isEqualTo(5)
        );
    }

    @Test
    @DisplayName("조회하려는 최단 구간의 출발역과 도착역은 달라야 한다")
    void 출발역과_도착역이_같은_경우() {
        // given
        PathFinder pathFinder = new PathFinder(Arrays.asList(신분당선, 이호선, 삼호선, 구호선));

        // when & then
        assertThatIllegalArgumentException().isThrownBy(
                () -> pathFinder.getShortestPath(고속터미널역, 고속터미널역)
        ).withMessageContaining("출발역과 도착역은 다른 역으로 지정되어야 합니다.");
    }

    @Test
    @DisplayName("조회하려는 최단 구간의 출발역과 도착역이 연결이 되어 있어야 한다")
    void 출발역과_도착역이_연결이_되어_있지_않은_경우() {
        // given
        PathFinder pathFinder = new PathFinder(Arrays.asList(신분당선, 삼호선));

        // when & then
        assertThatIllegalArgumentException().isThrownBy(
                () -> pathFinder.getShortestPath(고속터미널역, 신논현역)
        ).withMessageContaining("출발역과 도착역은 서로 연결이 되어있어야 합니다.");
    }

    @Test
    @DisplayName("존재하지 않는 출발역이나 도착역을 조회 할 경우 예외를 반환한다")
    void 존재하지_않는_출발역이나_도착역을_조회() {
        // given
        PathFinder pathFinder = new PathFinder(Arrays.asList(신분당선, 구호선));

        // when & then
        assertAll(
                () -> assertThatIllegalArgumentException().isThrownBy(
                        () -> pathFinder.getShortestPath(고속터미널역, 교대역)
                ).withMessageContaining("graph must contain"),
                () -> assertThatIllegalArgumentException().isThrownBy(
                        () -> pathFinder.getShortestPath(교대역, 고속터미널역)
                ).withMessageContaining("graph must contain")
        );
    }
}
