package nextstep.subway.path.domain.fare;

import java.util.Arrays;
import java.util.List;

import nextstep.subway.auth.domain.LoginMember;

public class FarePolicyFactory {
    private FarePolicyFactory() {
    }

    public static TotalFarePolicy createTotalFairPolicy(FarePolicy<Integer> distanceFarePolicy,
        FarePolicy<Fare> ageFairPolicy) {
        return new TotalFarePolicy(distanceFarePolicy, ageFairPolicy);
    }

    public static FarePolicy<Integer> createDistanceFairPolicy() {
        List<FarePolicy<Integer>> distancePolicies = Arrays.asList(DistanceFarePolicy.TEN_TO_FIFTY,
            DistanceFarePolicy.OVER_FIFTY);

        return (distance) -> {
            Fare fare = Fare.BASE;

            return distancePolicies.stream()
                .map(farePolicy -> farePolicy.calculateFare(distance))
                .reduce(fare, (Fare::add));
        };
    }

    public static FarePolicy<Fare> createAgeFairPolicy(LoginMember loginMember) {
        if (loginMember.isNotLogin()) {
            return (fare) -> fare;
        }

        return AgeFarePolicy.findPolicy(loginMember.getAge());
    }
}
