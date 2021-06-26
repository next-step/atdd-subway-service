package nextstep.subway.path.domain;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.wrapped.Money;

import static nextstep.subway.path.domain.age.AgeCalculator.calcAge;
import static nextstep.subway.path.domain.distance.DistanceCalculator.calcDistance;
import static nextstep.subway.path.domain.line.LineCalculator.calcLines;

public class FareCalculator {
    public static Money calcFare(LoginMember loginMember, ShortestDistance shortestDistance) {
        Money money = calcDistance(shortestDistance.shortestDistance());
        money = calcLines(new EfficientLines(shortestDistance.usedLines()), money);
        money = calcAge(loginMember, money);

        return money;
    }
}
