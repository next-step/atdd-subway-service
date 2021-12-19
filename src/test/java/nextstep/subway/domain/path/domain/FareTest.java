package nextstep.subway.domain.path.domain;

import nextstep.subway.domain.auth.domain.LoginUser;
import nextstep.subway.domain.line.domain.Distance;
import nextstep.subway.domain.line.domain.Line;
import nextstep.subway.domain.station.domain.Station;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("요금 계산")
class FareTest {

    @Test
    @DisplayName("기본 운임")
    void baseFare() {
        final Fare fare = new Fare(new Distance(10));
        Assertions.assertThat(fare).isEqualTo(new Fare(1250));
    }

    @ParameterizedTest
    @CsvSource(value = {"15:1350","50:2050", "58:2150", "106:2750"}, delimiter = ':')
    @DisplayName("이용거리 초과에 따른 추가 운임")
    void exceededDistance(int distance, int fareAmount) {
        final Fare fare = new Fare(new Distance(distance));
        Assertions.assertThat(fare).isEqualTo(new Fare(fareAmount));
    }

    @Test
    @DisplayName("노선별 추가 요금")
    void additionalFearForLine() {
        final List<Line> lines = Arrays.asList(
                new Line("신분당선", "bg-red-600", new Station("강남역"), new Station("양재역"), new Distance(10), 500),
                new Line("2호선", "bg-green-400", new Station("교대역"), new Station("강남역"), new Distance(10), 900),
                new Line("3호선", "bg-green-400", new Station("교대역"), new Station("양재역"), new Distance(5), 400));

        final Fare fare = new Fare(new Distance(12), lines);

        assertThat(fare).isEqualTo(new Fare(2250));
    }

    @ParameterizedTest
    @CsvSource(value = {"6:950","12:950","13:1520", "18:1520"},delimiter = ':')
    @DisplayName("연령별 할인")
    void discountByAge(int age, int expectFare) {
        final List<Line> lines = Arrays.asList(
                new Line("신분당선", "bg-red-600", new Station("강남역"), new Station("양재역"), new Distance(10), 500),
                new Line("2호선", "bg-green-400", new Station("교대역"), new Station("강남역"), new Distance(10), 900),
                new Line("3호선", "bg-green-400", new Station("교대역"), new Station("양재역"), new Distance(5), 400));

        final Fare fare = new Fare(new Distance(12), lines, new LoginUser(age));

        assertThat(fare).isEqualTo(new Fare(expectFare));
    }
}