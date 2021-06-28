package nextstep.subway.path.domain;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.line.domain.Line;
import nextstep.subway.path.domain.line.LinePolicyFixture;
import nextstep.subway.station.domain.Stations;
import nextstep.subway.wrapped.Distance;
import nextstep.subway.wrapped.Money;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.List;

import static nextstep.subway.path.domain.age.AgePolicyFixture.*;
import static org.assertj.core.api.Assertions.assertThat;

class FareCalculatorFacadeTest {
    @ParameterizedTest
    @CsvSource(value = {
            "10, 1950, false", "50, 2350, false", "80, 2500, false", // 비환승
            "10, 3950, true", "50, 4350, true", "80, 4500, true"  // 환승
    })
    @DisplayName("어린이 요금을 조회한다")
    public void 어린이_요금을_조회한다(int km, int exceptMoney, boolean transAt) {
        // given
        ShortestDistance shortestDistance = 최단거리_생성(km, transAt);
        LoginMember loginMember = new LoginMember(null, null, CHILD_AGE);

        // when
        Money money = FareCalculatorFacade.calcFare(loginMember, shortestDistance);

        // then
        assertThat(money).isEqualTo(new Money(exceptMoney));
    }

    @ParameterizedTest
    @CsvSource(value = {
            "10, 3120, false", "50, 3760, false", "80, 4000, false", // 비환승
            "10, 6320, true", "50, 6960, true", "80, 7200, true" // 환승
    })
    @DisplayName("청소년 요금을 조회한다")
    public void 청소년_요금을_조회한다(int km, int exceptMoney, boolean transAt) {
        // given
        ShortestDistance shortestDistance = 최단거리_생성(km, transAt);
        LoginMember loginMember = new LoginMember(null, null, TEENAGER_AGE);

        // when
        Money money = FareCalculatorFacade.calcFare(loginMember, shortestDistance);

        // then
        assertThat(money).isEqualTo(new Money(exceptMoney));
    }

    @ParameterizedTest
    @CsvSource(value = {
            "10, 4250, false", "50, 5050, false", "80, 5350, false", // 비환승
            "10, 8250, true", "50, 9050, true", "80, 9350, true" // 환승
    })
    @DisplayName("일반 요금을 조회한다")
    public void 일반_요금을_조회한다(int km, int exceptMoney, boolean transAt) {
        // given
        ShortestDistance shortestDistance = 최단거리_생성(km, transAt);
        LoginMember loginMember = new LoginMember(null, null, NORMAL_AGE);

        // when
        Money money = FareCalculatorFacade.calcFare(loginMember, shortestDistance);

        // then
        assertThat(money).isEqualTo(new Money(exceptMoney));
    }

    private ShortestDistance 최단거리_생성(int distance, boolean transAt) {
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
                if (transAt) {
                    return LinePolicyFixture.환승_최대요금_7000;
                }

                return LinePolicyFixture.비환승_요금_3000;
            }
        };
    }
}