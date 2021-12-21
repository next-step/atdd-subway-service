package nextstep.subway.path.domain.fare;

import java.util.Arrays;
import java.util.List;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;

public class FarePolicyFactory {
    private FarePolicyFactory() {
    }

    public static TotalFarePolicy createTotalFarePolicy(FarePolicy<Integer> distanceFarePolicy,
        FarePolicy<Fare>... farePolicies) {
        return new TotalFarePolicy(distanceFarePolicy, farePolicies);
    }

    public static FarePolicy<Integer> createDistanceFarePolicy() {
        List<FarePolicy<Integer>> distancePolicies = Arrays.asList(DistanceFarePolicy.TEN_TO_FIFTY,
            DistanceFarePolicy.OVER_FIFTY);

        return distance -> distancePolicies.stream()
            .map(farePolicy -> farePolicy.calculateFare(distance))
            .reduce(Fare.BASE, (Fare::add));
    }

    public static FarePolicy<Fare> createSectionFarePolicy(List<Section> sections,
        List<Station> stations) {
        return new SectionFarePolicy(sections, stations);
    }

    public static FarePolicy<Fare> createAgeFarePolicy(LoginMember loginMember) {
        if (loginMember.isNotLogin()) {
            return fare -> fare;
        }

        return AgeFarePolicy.findPolicy(loginMember.getAge());
    }
}
