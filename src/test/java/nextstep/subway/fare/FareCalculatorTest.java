package nextstep.subway.fare;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.fare.domain.Fare;
import nextstep.subway.fare.domain.FareCalculator;
import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.path.domain.Path;
import nextstep.subway.path.domain.ShortestPath;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

public class FareCalculatorTest {

    private Station 강남역;
    private Station 남부터미널역;
    private Station 양재역;
    private Station 교대역;
    private Station 건대입구역;
    private Station 잠실역;

    private Line 신분당선;
    private Line 이호선;
    private Line 삼호선;

    private LoginMember 비회원;
    private LoginMember 어린이;
    private LoginMember 청소년;

    private Path path;
    private ShortestPath 교대역_양재역_최단거리;
    private ShortestPath 양재역_건대입구역_최단거리;
    private ShortestPath 강남역_양재역_최단거리;
    private ShortestPath 교대역_강남역_최단거리;
    private ShortestPath 교대역_잠실역_최단거리;

    /**            거리 6                  거리 20                  거리 30
     * 교대역   --- *2호선* ---   강남역   --- *2호선* ---   잠실역   --- *2호선* ---   건대입구역
     * |                        |
     * *3호선*                   *신분당선*
     거리 3                    거리 12
     * |                        |
     * 남부터미널역  --- *3호선* --- 양재역
     *                거리 2
     */

    @BeforeEach
    void setUp() {
        교대역 = new Station("교대역");
        강남역 = new Station("강남역");
        잠실역 = new Station("잠실역");
        건대입구역 = new Station("건대입구역");
        남부터미널역 = new Station("남부터미널역");
        양재역 = new Station("양재역");

        이호선 = new Line("이호선", "초록", 교대역, 강남역, 6, 0);
        Section 강남역_잠실역 = new Section(이호선, 강남역, 잠실역, new Distance(20));
        Section 잠실역_건대입구역 = new Section(이호선, 잠실역, 건대입구역, new Distance(30));
        이호선.add(강남역_잠실역);
        이호선.add(잠실역_건대입구역);

        신분당선 = new Line("신분당선", "빨강", 강남역, 양재역, 12, 1000);

        삼호선 = new Line("삼호선", "주황", 교대역, 남부터미널역, 3, 500);
        Section 남부터미널역_양재역 = new Section(삼호선, 남부터미널역, 양재역, new Distance(2));
        삼호선.add(남부터미널역_양재역);

        비회원 = new LoginMember();
        어린이 = new LoginMember(1L, "children@email.com", 10);
        청소년 = new LoginMember(2L, "teenager@email.com", 15);

        path = Path.of(Arrays.asList(신분당선, 이호선, 삼호선));

        // 5km
        교대역_양재역_최단거리 = path.findShortestPath(교대역, 양재역);
        // 5km
        교대역_강남역_최단거리 = path.findShortestPath(교대역, 강남역);
        // 11km
        강남역_양재역_최단거리 = path.findShortestPath(강남역, 양재역);
        // 25km
        교대역_잠실역_최단거리 = path.findShortestPath(교대역, 잠실역);
        // 60km
        양재역_건대입구역_최단거리 = path.findShortestPath(양재역, 건대입구역);

    }

    @DisplayName("비회원 10km 이내 거리 요금 (5km)")
    @Test
    void calculateDistanceFare_10km_이내() {
        Fare fareResult = FareCalculator.of(비회원, 교대역_강남역_최단거리).calculate();
        assertThat(fareResult).isEqualTo(Fare.of(1250));
    }

    @DisplayName("비회원 10km 초과 거리 요금 (26km)")
    @Test
    void calculateDistanceFare_10km_초과() {
        Fare fareResult = FareCalculator.of(비회원, 교대역_잠실역_최단거리).calculate();
        assertThat(fareResult).isEqualTo(Fare.of(1650));
    }

    @DisplayName("비회원 10km 이내 노선별 추가요금 (거리:5km, 삼호선 추가요금:500원, 신분당선 추가요금:1000원)")
    @Test
    void calculateDistanceFare_10km_이내_노선별_추가요금() {
        Fare fareResult = FareCalculator.of(비회원, 교대역_양재역_최단거리).calculate();
        assertThat(fareResult).isEqualTo(Fare.of(1750));
    }

    @DisplayName("비회원 10km 초과 노선별 추가요금 (거리:11km, 3호선 추가요금:500원, 이호선 추가요금:0원)")
    @Test
    void calculateDistanceFare_10km_초과_노선별_추가요금() {
        Fare fareResult = FareCalculator.of(비회원, 강남역_양재역_최단거리).calculate();
        assertThat(fareResult).isEqualTo(Fare.of(1850));
    }

    @DisplayName("비회원 50km 초과 노선별 추가요금 (62km, 이호선 추가요금:0원, 신분당선 추가요금:1000원)")
    @Test
    void calculateDistanceFare_50km_초과() {
        Fare fareResult = FareCalculator.of(비회원, 양재역_건대입구역_최단거리).calculate();
        assertThat(fareResult).isEqualTo(Fare.of(2750));
    }

    @DisplayName("어린이 할인 요금 : 운임에서 350원 공제한 금액의 50%")
    @Test
    void calculateChildrenDiscountFare() {
        Fare fareResult = FareCalculator.of(어린이, 교대역_강남역_최단거리).calculate();
        assertThat(fareResult).isEqualTo(Fare.of(450));
    }

    @DisplayName("청소년 할인 요금 : 운임에서 350원 공제한 금액의 20%")
    @Test
    void calculateTeenagerDiscountFare() {
        Fare fareResult = FareCalculator.of(청소년, 교대역_강남역_최단거리).calculate();
        assertThat(fareResult).isEqualTo(Fare.of(720));
    }

}
