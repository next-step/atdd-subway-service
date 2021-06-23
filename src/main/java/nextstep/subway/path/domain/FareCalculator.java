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

public class FareCalculator {
    public static Money calc(List<Line> lines, LoginMember loginMember, ShortestDistance shortestDistance) {
        Money money = calcDistance(shortestDistance.shortestDistance(), new Money(0));
        money = calcAge(loginMember, money);
        money = calcLines(getThroughLines(new Lines(lines), shortestDistance), money);

        return money;
    }

    private static Money calcDistance(Distance distance, Money money) {
        List<DistancePremiumPolicy> distancePremiumPolicies = Arrays.asList(new DefaultDistancePremiumPolicy(),
                new MidRangeDistancePremiumPolicy(),
                new LongRangeDistancePremiumPolicy());

        for (DistancePremiumPolicy premiumPolicy : distancePremiumPolicies) {
            if (premiumPolicy.isSupport(distance)) {
                money = premiumPolicy.calcFare(distance, money);
            }
        }

        return money;
    }

    private static Money calcAge(LoginMember loginMember, Money money) {
        List<AgeDiscountPolicy> ageDiscountPolicies = Arrays.asList(
                new DefaultAgeDiscountPolicy(),
                new ChildDiscountPolicy(),
                new TeenagerDiscountPolicy()
        );

        for (AgeDiscountPolicy discountPolicy : ageDiscountPolicies) {
            if (discountPolicy.isSupport(loginMember)) {
                money = discountPolicy.calcFare(loginMember, money);
            }
        }

        return money;
    }

    private static Money calcLines(Lines lines, Money money) {
        DefaultLinePremiumPolicy premiumPolicy = new DefaultLinePremiumPolicy();

        if (premiumPolicy.isSupport(lines)) {
            money = premiumPolicy.calcFare(lines, money);
        }

        return money;
    }



    private static Lines getThroughLines(Lines lines, ShortestDistance shortestDistance) {
        Stations stations = shortestDistance.shortestRoute();
        List<SimpleSection> simpleSection = stations.getSimpleSection();

        return new Lines(simpleSection.stream()
                .map(item -> lines.findShortestSectionBy(item))
                .collect(Collectors.toList()));
    }
}
