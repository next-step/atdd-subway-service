package nextstep.subway.fare.policy.customer;

import java.util.Arrays;

import nextstep.subway.fare.exception.PolicyNotFoundException;
import nextstep.subway.member.domain.Member;

public enum CustomerType {
    ADULT(new AdultPolicy()),
    TEENAGER(new TeenagerPolicy()),
    CHILD(new ChildPolicy());

    CustomerPolicy policy;

    CustomerType(CustomerPolicy policy) {
        this.policy = policy;
    }

    public static CustomerPolicy getPolicy(Member member) {
        return Arrays.stream(values())
            .map(p -> p.policy)
            .filter(p -> p.isAvailable(member))
            .findAny()
            .orElseThrow(() -> new PolicyNotFoundException("해당 고객의 가격정책이 확인되지 않습니다."));
    }
}
