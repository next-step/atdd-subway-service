package nextstep.subway.path.policy;

import org.springframework.stereotype.Component;

@Component
public class DiscountPolicyByAgeResolver {
    public DiscountPolicy resolve(int age) {
        return new DefaultAgeDiscountPolicy(age);
    }
}
