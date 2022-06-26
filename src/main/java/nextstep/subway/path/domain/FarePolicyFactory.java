package nextstep.subway.path.domain;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Lines;
import nextstep.subway.station.domain.Station;

import java.util.List;

public class FarePolicyFactory {

    private FarePolicyFactory() {
    }

    public static FarePolicy of(LoginMember loginMember, Lines lines, ShortestPath shortestPath) {
        if (loginMember.isGuest()) {
            return noDisCountFarePolicy(lines, shortestPath);
        }
        return memberDisCountFarePolicy(loginMember, lines, shortestPath);
    }

    private static FarePolicy memberDisCountFarePolicy(LoginMember loginMember, Lines lines, ShortestPath shortestPath) {
        return new MemberFareDiscountPolicy(noDisCountFarePolicy(lines, shortestPath), loginMember.getAge());
    }

    private static DistanceFarePolicy noDisCountFarePolicy(Lines lines, ShortestPath shortestPath) {
        List<Station> path = shortestPath.getPath();
        int distance = shortestPath.getDistance();

        FarePolicy lineSurchargePolicy = new LineSurchargePolicy(() -> Fare.FREE, lines.getLinesInShortestPath(path));
        return new DistanceFarePolicy(lineSurchargePolicy, new Distance(distance));
    }
}
