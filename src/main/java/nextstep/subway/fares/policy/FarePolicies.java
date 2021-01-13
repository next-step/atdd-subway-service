package nextstep.subway.fares.policy;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.fares.domain.Fare;
import nextstep.subway.path.domain.Path;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class FarePolicies {

    private final List<FarePolicy> farePolicies;

    public FarePolicies() {
        farePolicies = Collections.unmodifiableList(Arrays.asList(
                new DistanceBasedFarePolicy(),
                new SectionExtraChargeFarePolicy(),
                new DiscountByAgeFarePolicy()
        ));
    }

    public Fare calculateFare(Path path, LoginMember loginMember) {
        Fare fare = new Fare();
        for (FarePolicy farePolicy : farePolicies) {
            farePolicy.calculateFare(fare, path, loginMember);
        }
        return fare;
    }
}
