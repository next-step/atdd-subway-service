package nextstep.subway.domain.path.domain;

import nextstep.subway.domain.auth.domain.AnonymousUser;
import nextstep.subway.domain.auth.domain.LoginUser;
import nextstep.subway.domain.line.domain.Distance;
import nextstep.subway.domain.line.domain.Line;
import nextstep.subway.domain.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
@DisplayName("요금 계산")
class FareTest {

    private Station 강남역;
    private Station 양재역;
    private Station 교대역;
    private Station 남부터미널역;
    private Line 신분당선;
    private Line 이호선;
    private Line 삼호선;
    private List<Line> lines;

    @BeforeEach
    void setUp() {
        강남역 = new Station(1L, "강남역");
        양재역 = new Station(2L, "양재역");
        교대역 = new Station(3L, "교대역");
        남부터미널역 = new Station(4L, "남부터미널역");

        신분당선 = new Line("신분당선", "bg-red-600", 강남역, 양재역, new Distance(10), 0);
        이호선 = new Line("이호선", "bg-green-400", 교대역, 강남역, new Distance(10), 0);
        삼호선 = new Line("삼호선", "bg-orange-200", 교대역, 양재역, new Distance(5), 0);
        삼호선.addSection(교대역, 남부터미널역, new Distance(3));
        lines = Arrays.asList(신분당선, 이호선, 삼호선);
    }

    @Test
    @DisplayName("기본 운임")
    void baseFare() {
        // given
        final PathFinder pathFinder = new PathFinder(lines);
        final Route shortestRoute = pathFinder.findShortestRoute(교대역.getId(), 양재역.getId(), lines);

        // when
        final Fare fare = Fare.calculate(shortestRoute, new AnonymousUser());

        // then
        assertThat(fare).isEqualTo(new Fare(1250));
    }

    @ParameterizedTest
    @CsvSource(value = {"15:1350","50:2050", "58:2150", "106:2750"}, delimiter = ':')
    @DisplayName("이용거리 초과에 따른 추가 운임")
    void exceededDistance(int distance, int fareAmount) {
        // given
        final PathFinder pathFinder = new PathFinder(lines);
        final Route shortestRoute = pathFinder.findShortestRoute(교대역.getId(), 양재역.getId(), lines);
        Route spyShortestRoute = Mockito.spy(shortestRoute);
        Mockito.when(spyShortestRoute.getDistance()).thenReturn(new Distance(distance));

        // when
        final Fare fare = Fare.calculate(spyShortestRoute, new AnonymousUser());

        assertThat(fare).isEqualTo(new Fare(fareAmount));
    }

    @Test
    @DisplayName("노선별 추가 요금")
    void additionalFearForLine() {
        // given
        신분당선.changeExtraFare(500);
        이호선.changeExtraFare(900);
        삼호선.changeExtraFare(400);
        final PathFinder pathFinder = new PathFinder(lines);
        final Route shortestRoute = pathFinder.findShortestRoute(교대역.getId(), 양재역.getId(), lines);
        Route spyShortestRoute = Mockito.spy(shortestRoute);
        Mockito.when(spyShortestRoute.getDistance()).thenReturn(new Distance(12));

        // when
        final Fare fare = Fare.calculate(spyShortestRoute, new AnonymousUser());

        // then
        assertThat(fare).isEqualTo(new Fare(1750));
    }

    @ParameterizedTest
    @CsvSource(value = {"6:450","13:720", "19:1250"},delimiter = ':')
    @DisplayName("노선별 추가 요금이 없는 연령별 할인")
    void noNAdditionalFearForLineAndDiscountByAge(int age, int expectFare) {
        // given
        final PathFinder pathFinder = new PathFinder(lines);
        final Route shortestRoute = pathFinder.findShortestRoute(교대역.getId(), 양재역.getId(), lines);
        Route spyShortestRoute = Mockito.spy(shortestRoute);
        Mockito.when(spyShortestRoute.getDistance()).thenReturn(new Distance(10));

        // when
        final Fare fare = Fare.calculate(spyShortestRoute, new LoginUser(age));

        assertThat(fare).isEqualTo(new Fare(expectFare));
    }

    @ParameterizedTest
    @CsvSource(value = {"6:700","12:700","13:1120", "18:1120"},delimiter = ':')
    @DisplayName("연령별 할인")
    void discountByAge(int age, int expectFare) {
        // given
        신분당선.changeExtraFare(500);
        이호선.changeExtraFare(900);
        삼호선.changeExtraFare(400);
        final PathFinder pathFinder = new PathFinder(lines);
        final Route shortestRoute = pathFinder.findShortestRoute(교대역.getId(), 양재역.getId(), lines);
        Route spyShortestRoute = Mockito.spy(shortestRoute);
        Mockito.when(spyShortestRoute.getDistance()).thenReturn(new Distance(12));

        // when
        final Fare fare = Fare.calculate(spyShortestRoute, new LoginUser(age));

        assertThat(fare).isEqualTo(new Fare(expectFare));
    }
}