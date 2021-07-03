package nextstep.subway.path.domain.policy.fare;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.path.domain.ShortestDistance;
import nextstep.subway.path.domain.policy.PolicyConfig;
import nextstep.subway.path.domain.policy.fare.discount.DiscountByAgeStrategy;
import nextstep.subway.path.domain.policy.fare.distance.OverFareByDistanceStrategy;
import nextstep.subway.path.domain.policy.fare.line.DefaultOverFareByLineStrategy;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Objects;

@Component
public class FarePoliciesService {
    public FarePolicies findStrategies(LoginMember loginMember, ShortestDistance distance) {
        FarePolicies farePolicies = new FarePolicies();
        AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(PolicyConfig.class);

        setDistanceStrategy(distance, farePolicies, ac);
        setDiscountStrategy(loginMember, farePolicies, ac);
        setLineStrategy(farePolicies, ac);
        return farePolicies;
    }

    private void setLineStrategy(FarePolicies farePolicies, AnnotationConfigApplicationContext ac) {
        farePolicies.changeLineStrategy(ac.getBean(DefaultOverFareByLineStrategy.class));
    }

    private void setDiscountStrategy(LoginMember loginMember, FarePolicies farePolicies, AnnotationConfigApplicationContext ac) {
        if (!Objects.isNull(loginMember.getId())) {
            Map<String, DiscountByAgeStrategy> discountByAgeStrategyBeans = ac.getBeansOfType(DiscountByAgeStrategy.class);
            discountByAgeStrategyBeans.keySet().stream()
                    .filter(key -> discountByAgeStrategyBeans.get(key).isAvailable(loginMember))
                    .map(key -> discountByAgeStrategyBeans.get(key))
                    .findFirst()
                    .ifPresent(strategy -> farePolicies.changeDiscountByAgeStrategy(strategy));
        }
    }

    private void setDistanceStrategy(ShortestDistance distance, FarePolicies farePolicies, AnnotationConfigApplicationContext ac) {
        Map<String, OverFareByDistanceStrategy> overFareByDistanceStrategyBeans = ac.getBeansOfType(OverFareByDistanceStrategy.class);
        overFareByDistanceStrategyBeans.keySet().stream()
                .filter(key -> overFareByDistanceStrategyBeans.get(key).isAvailable(distance))
                .map(key -> overFareByDistanceStrategyBeans.get(key))
                .findFirst()
                .ifPresent(strategy -> farePolicies.changeDistanceStrategy(strategy));
    }
}
