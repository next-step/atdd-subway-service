package nextstep.subway.fare.policy;

import nextstep.subway.auth.domain.AuthMember;
import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.fare.policy.discount.ChildPolicy;
import nextstep.subway.fare.policy.discount.DiscountPolicy;
import nextstep.subway.fare.policy.discount.NoneLoginPolicy;
import nextstep.subway.fare.policy.discount.YouthPolicy;
import nextstep.subway.fare.policy.extra.LineExtraFarePolicy;

import java.util.ArrayList;
import java.util.List;

public class Policies {
    private final List<DiscountPolicy> discountPolicies;

    public Policies(AuthMember authMember) {
        this.discountPolicies = initPolicy(authMember);
    }

    public List<DiscountPolicy> getDiscountPolicies() {
        return discountPolicies;
    }

    private List<DiscountPolicy> initPolicy(AuthMember authMember) {
        List<DiscountPolicy> policies = new ArrayList<>();
        addLoginUserPolicy(authMember, policies);
        addNonLoginUserPolicy(authMember, policies);
        return policies;
    }

    private void addLoginUserPolicy(AuthMember authMember, List<DiscountPolicy> policies) {
        if (isLoginMember(authMember)) {
            policies.add(new ChildPolicy(authMember.getAge(), new LineExtraFarePolicy()));
            policies.add(new YouthPolicy(authMember.getAge(), new LineExtraFarePolicy()));
        }
    }

    private void addNonLoginUserPolicy(AuthMember authMember, List<DiscountPolicy> policies) {
        if (!isLoginMember(authMember)) {
            policies.add(new NoneLoginPolicy(new LineExtraFarePolicy()));
        }
    }

    private boolean isLoginMember(AuthMember authMember) {
        return authMember instanceof LoginMember;
    }
}
