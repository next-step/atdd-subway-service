package nextstep.subway.path.domain;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.path.domain.age.AgeCalculator;
import nextstep.subway.path.domain.distance.DistanceCalculator;
import nextstep.subway.path.domain.line.LineCalculator;
import nextstep.subway.wrapped.Money;

import java.util.Arrays;
import java.util.List;

public class FareCalculatorFacade {
    private static final List<Calculator> calculators = Arrays.asList(
            new DistanceCalculator(),
            new LineCalculator(),
            new AgeCalculator()
    );

    public static Money calcFare(LoginMember loginMember, ShortestDistance shortestDistance) {
        Money money = new Money(0);

        for (Calculator calculator : calculators) {
            money = calculator.calc(money, loginMember, shortestDistance);
        }

        return money;
    }
}
