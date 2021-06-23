package nextstep.subway.path.domain;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Lines;
import nextstep.subway.line.domain.SimpleSection;
import nextstep.subway.path.domain.age.ChildDiscountPolicy;
import nextstep.subway.path.domain.age.DefaultAgeDiscountPolicy;
import nextstep.subway.path.domain.age.TeenagerDiscountPolicy;
import nextstep.subway.path.domain.distance.DefaultDistancePremiumPolicy;
import nextstep.subway.path.domain.distance.LongRangeDistancePremiumPolicy;
import nextstep.subway.path.domain.distance.MidRangeDistancePremiumPolicy;
import nextstep.subway.path.domain.line.DefaultLinePremiumPolicy;
import nextstep.subway.station.domain.Stations;
import nextstep.subway.wrapped.Distance;
import nextstep.subway.wrapped.Money;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static nextstep.subway.path.domain.age.AgeCalculator.calcAge;
import static nextstep.subway.path.domain.distance.DistanceCalculator.calcDistance;
import static nextstep.subway.path.domain.line.LineCalculator.calcLines;

public class FareCalculator {
    public static Money calc(LoginMember loginMember, ShortestDistance shortestDistance) {
        Money money = calcDistance(shortestDistance.shortestDistance(), new Money(0));
        money = calcAge(loginMember, money);
        money = calcLines(shortestDistance.usedLines(), money);

        return money;
    }
}
