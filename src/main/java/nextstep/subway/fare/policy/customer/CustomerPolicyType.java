package nextstep.subway.fare.policy.customer;

import java.util.Arrays;

import nextstep.subway.fare.exception.PolicyNotFoundException;
import nextstep.subway.fare.policy.FarePolicy;
import nextstep.subway.member.domain.Member;

public enum CustomerPolicyType {
    ADULT(new CustomerAgeDiscountFarePolicy(
        20, 200, fare -> fare)),
    TEENAGER(new CustomerAgeDiscountFarePolicy(
        13, 20, fare -> fare.subtract(350).multiplyBy(0.8))),
    CHILD(new CustomerAgeDiscountFarePolicy(
        6, 13, fare -> fare.subtract(350).multiplyBy(0.5)));

    CustomerAgeDiscountFarePolicy policy;

    CustomerPolicyType(CustomerAgeDiscountFarePolicy policy) {
        this.policy = policy;
    }

    public static CustomerPolicyType of(Member member) {
        return Arrays.stream(values())
            .filter(type -> type.policy.includes(member.getAge()))
            .findAny()
            .orElseThrow(() -> new PolicyNotFoundException("해당 고객의 가격정책이 확인되지 않습니다."));
    }

    public FarePolicy getPolicy() {
        return policy.getFarePolicy();
    }
}
