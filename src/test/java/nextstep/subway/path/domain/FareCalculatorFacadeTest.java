package nextstep.subway.path.domain;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.line.domain.Line;
import nextstep.subway.station.domain.Stations;
import nextstep.subway.wrapped.Distance;
import nextstep.subway.wrapped.Money;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;

class FareCalculatorFacadeTest {
    private static final int childAge = 6;
    private static final int teenagerAge = 13;
    private static final int normalAge = 19;

    private static final int shortDistance = 10;
    private static final int midDistance = 50;
    private static final int longDistance = 80;

    private static final int shortDistanceFare = 1250;
    private static final int midDistanceFare = shortDistanceFare + ((midDistance - shortDistance) / 5 * 100);
    private static final int longDistanceFare = midDistanceFare + ((longDistance - midDistance) / 8 * 100);

    private static final int transFare = 7000;
    private static final int noTransFare = 3000;

    private static final int distances[] = new int[]{shortDistance, midDistance, longDistance};
    private static final int distanceFares[] = new int[]{shortDistanceFare, midDistanceFare, longDistanceFare};

    private static final Function<Integer, Money> 어린이_할인_계산 = (money) -> new Money((money - 350) / 10 * 5);
    private static final Function<Integer, Money> 청소년_할인_계산 = (money) -> new Money((money - 350) / 10 * 8);
    private static final Function<Integer, Money> 일반_할인_계산 = (money) -> new Money(money);
    @ParameterizedTest
    @MethodSource("childCase")
    @DisplayName("어린이의 요금을 계산한다(짧은거리, 중간거리, 장거리, 환승, 비환승)")
    public void 어린이(FareCalculatorCase fareCalculatorCase) {
        assertThat(FareCalculatorFacade.calcFare(new LoginMember(null, null, childAge), fareCalculatorCase.getShortestDistance()))
                .isEqualTo(fareCalculatorCase.getExceptMoney());
    }

    @ParameterizedTest
    @MethodSource("teenagerCase")
    @DisplayName("청소년의 요금을 계산한다(짧은거리, 중간거리, 장거리, 환승, 비환승)")
    public void 청소년(FareCalculatorCase fareCalculatorCase) {
        assertThat(FareCalculatorFacade.calcFare(new LoginMember(null, null, teenagerAge), fareCalculatorCase.getShortestDistance()))
                .isEqualTo(fareCalculatorCase.getExceptMoney());
    }

    @ParameterizedTest
    @MethodSource("defaultCase")
    @DisplayName("일반의 요금을 계산한다(짧은거리, 중간거리, 장거리, 환승, 비환승)")
    public void 일반(FareCalculatorCase fareCalculatorCase) {
        assertThat(FareCalculatorFacade.calcFare(new LoginMember(null, null, normalAge), fareCalculatorCase.getShortestDistance()))
                .isEqualTo(fareCalculatorCase.getExceptMoney());
    }

    public static List<FareCalculatorCase> childCase() {
        return createCase(어린이_할인_계산);
    }

    public static List<FareCalculatorCase> teenagerCase() {
        return createCase(청소년_할인_계산);
    }

    public static List<FareCalculatorCase> defaultCase() {
        return createCase(일반_할인_계산);
    }

    public static List<FareCalculatorCase> createCase(Function<Integer, Money> calc) {
        List<FareCalculatorCase> results = new ArrayList<>();
        for (int i = 0; i < distances.length; i++) {
            results.add(
                    new FareCalculatorCase(
                            비환승_케이스(distances[i]),
                            calc.apply(noTransFare + distanceFares[i])
                    )
            );
            results.add(
                    new FareCalculatorCase(
                            환승_케이스(distances[i]),
                            calc.apply(transFare + distanceFares[i])
                    )
            );
        }

        return results;
    }

    static class FareCalculatorCase {
        private ShortestDistance shortestDistance;
        private Money exceptMoney;

        public FareCalculatorCase(ShortestDistance shortestDistance, Money exceptMoney) {
            this.shortestDistance = shortestDistance;
            this.exceptMoney = exceptMoney;
        }

        public ShortestDistance getShortestDistance() {
            return shortestDistance;
        }

        public Money getExceptMoney() {
            return exceptMoney;
        }
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