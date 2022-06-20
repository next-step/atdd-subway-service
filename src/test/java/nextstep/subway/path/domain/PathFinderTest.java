package nextstep.subway.path.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.Arrays;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PathFinderTest {

    private Station 고속터미널역;
    private Station 교대역;
    private Station 강남역;
    private Station 신논현역;
    private Station 판교역;
    private Line 신분당선;
    private Line 이호선;
    private Line 삼호선;
    private Line 구호선;

    /**
     * 고속터미널역 --- *9호선*(4) --- 신논현역
     * |                            |
     * *3호선(3)*                 *신분당선*(1)
     * |                            |
     * 교대역   --- *2호선*(3) ---   강남역  -- *신분당선*(50) --- 판교역
     */
    @BeforeEach
    public void setUp() {
        고속터미널역 = new Station("고속터미널역");
        교대역 = new Station("교대역");
        강남역 = new Station("강남역");
        신논현역 = new Station("신논현역");
        판교역 = new Station("판교역");

        신분당선 = new Line("신분당선", "red", 신논현역, 강남역, 1, 1000);
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
        ShortestPath shortestPath = pathFinder.getShortestPath(고속터미널역, 강남역);

        // then
        assertAll(
                () -> assertThat(shortestPath.getShortestStations()).containsExactly(고속터미널역, 신논현역, 강남역),
                () -> assertThat(shortestPath.getShortestDistance()).isEqualTo(5)
        );
    }

    /* 강남역 --- *신분당선*(50) --- 판교역 */
    @Test
    @DisplayName("추가 요금 1000원이 있는 노선의 50km 이용 시 이용 요금은 3050원")
    void calculateAdditionalFare() {
        // given
        신분당선.addSection(new Section(신분당선, 강남역, 판교역, 50));
        PathFinder pathFinder = new PathFinder(Arrays.asList(신분당선, 이호선, 삼호선, 구호선));

        // when
        ShortestPath shortestPath = pathFinder.getShortestPath(강남역, 판교역);

        // then
        assertThat(shortestPath.getFare()).isEqualTo(3050);
    }

    /* 신림역 --- *신림선*(4) --- 교대역 --- *2호선*(3) --- 강남역 --- *신분당선*(1) --- 신논현역 */
    @Test
    @DisplayName("0원, 500원, 1000원의 추가 요금이 있는 노선들을 경유하여 8km 이용시 2250원")
    void calculateAdditionalFareByTransferLine() {
        // given
        Station 신림역 = new Station("신림역");
        Line 신림선 = new Line("신림선", "blue", 신림역, 교대역, 4, 500);
        PathFinder pathFinder = new PathFinder(Arrays.asList(신분당선, 이호선, 삼호선, 구호선, 신림선));

        // when
        ShortestPath shortestPath = pathFinder.getShortestPath(신림역, 신논현역);

        // then
        assertThat(shortestPath.getFare()).isEqualTo(2250);
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
