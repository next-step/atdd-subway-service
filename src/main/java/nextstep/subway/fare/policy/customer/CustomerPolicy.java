package nextstep.subway.fare.policy.customer;

import java.util.ArrayList;
import java.util.List;

import nextstep.subway.member.domain.Member;
import nextstep.subway.fare.domain.Fare;
import nextstep.subway.fare.exception.PolicyNotFoundException;

public abstract class CustomerPolicy {
    protected static final List<CustomerPolicy> policies = new ArrayList<>();

    public abstract Fare apply(Fare fare);
    public abstract boolean isAvailable(Member member);

    protected CustomerPolicy() {
    }

    protected static void addPolicy(CustomerPolicy policy) {
        policies.add(policy);
    }

    public static CustomerPolicy getCustomerPolicy(Member member) {
        return policies.stream()
            .filter(p -> p.isAvailable(member))
            .findAny()
            .orElseThrow(() -> new PolicyNotFoundException("해당 고객의 가격정책이 확인되지 않습니다."));
    }

}
