package nextstep.subway.fare.domain;

import static org.assertj.core.api.Assertions.assertThat;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Lines;
import nextstep.subway.line.domain.Section;
import nextstep.subway.path.domain.DijkstraPathFinder;
import nextstep.subway.path.domain.Path;
import nextstep.subway.path.domain.PathFinder;
import nextstep.subway.policy.NonDiscountPolicy;
import nextstep.subway.station.domain.Station;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class FareTest {

    private static final int NBD_LINE_FARE = 1500;
    private static final int TRD_LINE_FARE = 500;

    private Line 신분당선;
    private Line 삼호선;
    private Station 강남역;
    private Station 양재역;
    private Station 교대역;
    private Station 남부터미널역;

    /**
     * 교대역    --- *2호선* ---   강남역
     * |                        |
     * *3호선*                   *신분당선*
     * |                        |
     * 남부터미널역  --- *3호선* ---   양재
     */
    @BeforeEach
    void setUp() {
        강남역 = Station.of(1L, "강남역");
        양재역 = Station.of(2L, "양재역");
        교대역 = Station.of(3L, "교대역");
        남부터미널역 = Station.of(4L, "남부터미널역");

        신분당선 = Line.of("신분당선", "RED", 강남역, 양재역, 80, NBD_LINE_FARE);
        삼호선 = Line.of("삼호선", "ORANGE", 남부터미널역, 양재역, 15, TRD_LINE_FARE);
        삼호선.addSection(Section.of(교대역, 남부터미널역, 3));
    }

    @DisplayName("노선에 운임료가 있는 경우 가장 높은 운임료가 추가된다.")
    @Test
    void fareTest01() {
        // given
        Lines lines = Lines.from(Lists.newArrayList(신분당선, 삼호선));
        PathFinder pathFinder = DijkstraPathFinder.from(lines);
        Path path = pathFinder.findShortPath(남부터미널역, 강남역);
        Fare fare = new Fare(lines, path);

        // when
        int lineFare = fare.findLineFare();

        // then
        /**
         *  남부터미널 -<3호선>- 양재역 - <신분당선> - 강남역
         */
        assertThat(lineFare).isEqualTo(NBD_LINE_FARE);
    }

    @DisplayName("거리에 따라 거리별 추가 요금이 부과된다. (10km 이하)")
    @Test
    void fareTest02() {
        // given
        Lines lines = Lines.from(Lists.newArrayList(신분당선, 삼호선));
        PathFinder pathFinder = DijkstraPathFinder.from(lines);
        Path path = pathFinder.findShortPath(교대역, 남부터미널역);
        Fare fare = new Fare(lines, path);

        // when
        int overFare = fare.getOverFare();

        // then
        /**
         *  교대역 - <삼호선 : 3km> - 남부터미널역
         *  expected overDistance = 0
         *  expected overFare = 0
         */
        assertThat(overFare).isZero();
    }

    @DisplayName("거리에 따라 거리별 추가 요금이 부과된다. (10km 초과 ~ 50km까지)")
    @Test
    void fareTest03() {
        // given
        Lines lines = Lines.from(Lists.newArrayList(신분당선, 삼호선));
        PathFinder pathFinder = DijkstraPathFinder.from(lines);
        Path path = pathFinder.findShortPath(남부터미널역, 양재역);
        Fare fare = new Fare(lines, path);

        // when
        int overFare = fare.getOverFare();

        // then
        /**
         *  남부터미널역 - <삼호선 : 15km> - 양재역
         *  expected overDistance = 5km
         *  expected overFare = 100
         */
        assertThat(overFare).isEqualTo(100);
    }

    @DisplayName("거리에 따라 거리별 추가 요금이 부과된다. (50km 초과)")
    @Test
    void fareTest04() {
        // given
        Lines lines = Lines.from(Lists.newArrayList(신분당선, 삼호선));
        PathFinder pathFinder = DijkstraPathFinder.from(lines);
        Path path = pathFinder.findShortPath(강남역, 양재역);
        Fare fare = new Fare(lines, path);

        // when
        int overFare = fare.getOverFare();

        // then
        /**
         *  강남역 - <신분당선 : 80km> - 양재역
         *  expected overDistance = 70km
         *  expected overFare = 900
         */
        assertThat(overFare).isEqualTo(900);
    }

    @DisplayName("거리별 추가금액과 노선별 추가요금에 따라 총 요금이 계산된다.")
    @Test
    void totalFareTest01() {
        // given
        Lines lines = Lines.from(Lists.newArrayList(신분당선, 삼호선));
        PathFinder pathFinder = DijkstraPathFinder.from(lines);
        Path path = pathFinder.findShortPath(강남역, 양재역);
        Fare fare = new Fare(lines, path);

        // when
        int totalFare = fare.findTotalFare(new NonDiscountPolicy());

        // then
        /**
         *  강남역 - <신분당선 : 80km> - 양재역
         *  expected totalFare = 1250(기본료) + 900(초과거리) + 1500(노선추가금) = 3650
         */
        assertThat(totalFare).isEqualTo(3650);

    }
}