package nextstep.subway.path.domain.policy.fare;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.path.domain.ShortestDistance;
import nextstep.subway.path.domain.policy.PolicyConfig;
import nextstep.subway.path.domain.policy.fare.discount.DiscountByAgeStrategy;
import nextstep.subway.path.domain.policy.fare.distance.OverFareByDistanceStrategy;
import nextstep.subway.path.domain.policy.fare.line.DefaultOverFareByLineStrategy;
import nextstep.subway.path.domain.policy.fare.line.OverFareByLineStrategy;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Component
public class FarePoliciesService {
    public FarePolicies findStrategies(LoginMember loginMember, ShortestDistance distance) {
        FarePolicies farePolicies = new FarePolicies();
        setDistanceStrategy(distance, farePolicies);
        setDiscountStrategy(loginMember, farePolicies);
        setLineStrategy(farePolicies);
        return farePolicies;
    }

    private void setLineStrategy(FarePolicies farePolicies) {
        List<Object> defaultOverFareByLineStrategies = PolicyConfig.get(OverFareByLineStrategy.class);
        farePolicies.changeLineStrategy((DefaultOverFareByLineStrategy)defaultOverFareByLineStrategies.get(0));
    }

    private void setDiscountStrategy(LoginMember loginMember, FarePolicies farePolicies) {
        if (!Objects.isNull(loginMember.getId())) {
            List<Object> discountByAgeStrategies = PolicyConfig.get(DiscountByAgeStrategy.class);
            discountByAgeStrategies.stream()
                    .filter(strategy -> ((DiscountByAgeStrategy)strategy).isAvailable(loginMember))
                    .findFirst()
                    .ifPresent(strategy -> farePolicies.changeDiscountByAgeStrategy((DiscountByAgeStrategy)strategy));
        }
    }

    private void setDistanceStrategy(ShortestDistance distance, FarePolicies farePolicies) {
        List<Object> overFareByDistanceStrategies = PolicyConfig.get(OverFareByDistanceStrategy.class);
        overFareByDistanceStrategies.stream()
                .filter(strategy -> ((OverFareByDistanceStrategy)strategy).isAvailable(distance))
                .findFirst()
                .ifPresent(strategy -> farePolicies.changeDistanceStrategy((OverFareByDistanceStrategy)strategy));
    }
}
