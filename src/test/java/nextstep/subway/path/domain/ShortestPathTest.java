package nextstep.subway.path.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.Arrays;
import nextstep.subway.fare.domain.AgeDiscount;
import nextstep.subway.fare.domain.DistanceFare;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ShortestPathTest {

    private Station 고속터미널역;
    private Station 교대역;
    private Station 강남역;
    private Station 신논현역;
    private Station 잠실역;
    private Line 신분당선;
    private Line 이호선;
    private Line 삼호선;
    private Line 구호선;

    /**
     * 고속터미널역 --- *9호선*(4) --- 신논현역
     * |                            |
     * *3호선(3)*                 *신분당선*(1)
     * |                            |
     * 교대역   --- *2호선*(3) ---   강남역   --- *2호선*(10) --- 잠실역
     */
    @BeforeEach
    public void setUp() {
        고속터미널역 = new Station("고속터미널역");
        교대역 = new Station("교대역");
        강남역 = new Station("강남역");
        신논현역 = new Station("신논현역");
        잠실역 = new Station("잠실역");

        신분당선 = new Line("신분당선", "red", 신논현역, 강남역, 1, 1000);
        이호선 = new Line("이호선", "green", 교대역, 강남역, 3);
        삼호선 = new Line("삼호선", "orange", 고속터미널역, 교대역, 3);
        구호선 = new Line("구호선", "gold", 고속터미널역, 신논현역, 4);

        이호선.addSection(new Section(이호선, 강남역, 잠실역, 10));
    }

    /* 강남역 --- *신분당선*(50) --- 판교역 */
    @Test
    @DisplayName("추가 요금 1000원이 있는 노선의 50km 이용 시 이용 요금은 3050원")
    void calculateAdditionalFare() {
        // given
        Station 판교역 = new Station("판교역");
        신분당선.addSection(new Section(신분당선, 강남역, 판교역, 50));

        PathFinder pathFinder = new PathFinder(Arrays.asList(신분당선, 이호선, 삼호선, 구호선));
        ShortestPath shortestPath = pathFinder.getShortestPath(강남역, 판교역);

        // when
        int fare = shortestPath.getFare(new DistanceFare(), new AgeDiscount(20));

        // then
        assertThat(fare).isEqualTo(3050);
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
        assertThat(shortestPath.getFare(new DistanceFare(), new AgeDiscount(20))).isEqualTo(2250);
    }

    @Test
    @DisplayName("성인의 교대역 - 신논현역 이용 요금은 2050원")
    void getFare() {
        // given
        PathFinder pathFinder = new PathFinder(Arrays.asList(신분당선, 이호선, 삼호선, 구호선));
        ShortestPath path = pathFinder.getShortestPath(교대역, 신논현역);

        // when
        int fare = path.getFare(new DistanceFare(), new AgeDiscount(20));

        // then
        assertThat(fare).isEqualTo(2250);
    }

    @Test
    @DisplayName("청소년의 교대역 - 잠실역 이용 요금은 800원")
    void getFareForTeenager() {
        // given
        PathFinder pathFinder = new PathFinder(Arrays.asList(신분당선, 이호선, 삼호선, 구호선));
        ShortestPath path = pathFinder.getShortestPath(교대역, 잠실역);

        // when
        int fare = path.getFare(new DistanceFare(), new AgeDiscount(13));

        // then
        assertThat(fare).isEqualTo(800);
    }

    @Test
    @DisplayName("어린이의 교대역 - 잠실역 이용 요금은 500원")
    void getFareForChild() {
        // given
        PathFinder pathFinder = new PathFinder(Arrays.asList(신분당선, 이호선, 삼호선, 구호선));
        ShortestPath path = pathFinder.getShortestPath(교대역, 잠실역);

        // when
        int fare = path.getFare(new DistanceFare(), new AgeDiscount(6));

        assertThat(fare).isEqualTo(500);
    }

    @Test
    @DisplayName("노인과 유아는 이용 요금을 부과하지 않는다")
    void getFareForPreferential() {
        // given
        PathFinder pathFinder = new PathFinder(Arrays.asList(신분당선, 이호선, 삼호선, 구호선));
        ShortestPath path = pathFinder.getShortestPath(교대역, 잠실역);

        // when
        int seniorFare = path.getFare(new DistanceFare(), new AgeDiscount(70));
        int babyFare = path.getFare(new DistanceFare(), new AgeDiscount(2));

        // then
        assertAll(
                () -> assertThat(seniorFare).isZero(),
                () -> assertThat(babyFare).isZero()
        );
    }
}
