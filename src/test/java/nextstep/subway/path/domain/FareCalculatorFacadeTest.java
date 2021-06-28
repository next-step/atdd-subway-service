package nextstep.subway.path.domain;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.line.domain.Line;
import nextstep.subway.station.domain.Stations;
import nextstep.subway.wrapped.Distance;
import nextstep.subway.wrapped.Money;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.Arrays;
import java.util.List;

import static nextstep.subway.path.domain.age.AgePolicyFixture.*;
import static org.assertj.core.api.Assertions.assertThat;

class FareCalculatorFacadeTest {
    @ParameterizedTest
    @CsvSource(value = {"10, 1950", "50, 2350", "80, 2500"})
    @DisplayName("어린이 비환승 요금을 조회한다")
    public void 어린이_비환승_요금을_조회한다(int km, int exceptMoney) {
        // given
        ShortestDistance shortestDistance = 비환승_케이스(km);
        LoginMember loginMember = new LoginMember(null, null, CHILD_AGE);

        // when
        Money money = FareCalculatorFacade.calcFare(loginMember, shortestDistance);

        // then
        assertThat(money).isEqualTo(new Money(exceptMoney));
    }

    @ParameterizedTest
    @CsvSource(value = {"10, 3950", "50, 4350", "80, 4500"})
    @DisplayName("어린이 환승 요금을 조회한다")
    public void 어린이_환승_요금을_조회한다(int km, int exceptMoney) {
        // given
        ShortestDistance shortestDistance = 환승_케이스(km);
        LoginMember loginMember = new LoginMember(null, null, CHILD_AGE);

        // when
        Money money = FareCalculatorFacade.calcFare(loginMember, shortestDistance);

        // then
        assertThat(money).isEqualTo(new Money(exceptMoney));
    }

    @ParameterizedTest
    @CsvSource(value = {"10, 3120", "50, 3760", "80, 4000"})
    @DisplayName("청소년 비환승 요금을 조회한다")
    public void 청소년_비환승_요금을_조회한다(int km, int exceptMoney) {
        // given
        ShortestDistance shortestDistance = 비환승_케이스(km);
        LoginMember loginMember = new LoginMember(null, null, TEENAGER_AGE);

        // when
        Money money = FareCalculatorFacade.calcFare(loginMember, shortestDistance);

        // then
        assertThat(money).isEqualTo(new Money(exceptMoney));
    }

    @ParameterizedTest
    @CsvSource(value = {"10, 6320", "50, 6960", "80, 7200"})
    @DisplayName("청소년 환승 요금을 조회한다")
    public void 청소년_환승_요금을_조회한다(int km, int exceptMoney) {
        // given
        ShortestDistance shortestDistance = 환승_케이스(km);
        LoginMember loginMember = new LoginMember(null, null, TEENAGER_AGE);

        // when
        Money money = FareCalculatorFacade.calcFare(loginMember, shortestDistance);

        // then
        assertThat(money).isEqualTo(new Money(exceptMoney));
    }

    @ParameterizedTest
    @CsvSource(value = {"10, 4250", "50, 5050", "80, 5350"})
    @DisplayName("일반 비환승 요금을 조회한다")
    public void 일반_비환승_요금을_조회한다(int km, int exceptMoney) {
        // given
        ShortestDistance shortestDistance = 비환승_케이스(km);
        LoginMember loginMember = new LoginMember(null, null, NORMAL_AGE);

        // when
        Money money = FareCalculatorFacade.calcFare(loginMember, shortestDistance);

        // then
        assertThat(money).isEqualTo(new Money(exceptMoney));
    }

    @ParameterizedTest
    @CsvSource(value = {"10, 8250", "50, 9050", "80, 9350"})
    @DisplayName("일반 환승 요금을 조회한다")
    public void 일반_환승_요금을_조회한다(int km, int exceptMoney) {
        // given
        ShortestDistance shortestDistance = 환승_케이스(km);
        LoginMember loginMember = new LoginMember(null, null, NORMAL_AGE);

        // when
        Money money = FareCalculatorFacade.calcFare(loginMember, shortestDistance);

        // then
        assertThat(money).isEqualTo(new Money(exceptMoney));
    }

    private static ShortestDistance 환승_케이스(int distance) {
        return new ShortestDistance() {

            @Override
            public Distance shortestDistance() {
                return new Distance(distance);
            }

            @Override
            public Stations shortestRoute() {
                throw new UnsupportedOperationException("금액 게산시 사용하지 않는 Operation 입니다.");
            }

            @Override
            public List<Line> usedLines() {
                return Arrays.asList(
                        new Line("1호선", "1호선", 1000),
                        new Line("2호선", "2호선", 2000),
                        new Line("3호선", "3호선", 3000),
                        new Line("4호선", "4호선", 500),
                        new Line("5호선", "5호선", 7000)
                );
            }
        };
    }

    private static ShortestDistance 비환승_케이스(int distance) {
        return new ShortestDistance() {

            @Override
            public Distance shortestDistance() {
                return new Distance(distance);
            }

            @Override
            public Stations shortestRoute() {
                throw new UnsupportedOperationException("금액 게산시 사용하지 않는 Operation 입니다.");
            }

            @Override
            public List<Line> usedLines() {
                return Arrays.asList(
                        new Line("3호선", "3호선", 3000)
                );
            }
        };
    }
}