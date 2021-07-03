package nextstep.subway.fare.policy.customer;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import nextstep.subway.fare.domain.Fare;
import nextstep.subway.member.MemberTest;

@DisplayName("요금 정책 단위 테스트")
class CustomerPolicyTest {
    @Test
    @DisplayName("고객_할인적용_검증_테스트")
    void 고객_할인적용_검증_테스트() {
        CustomerPolicy adultPolicy = CustomerPolicy.getCustomerPolicy(MemberTest.일반_멤버);
        CustomerPolicy teenagerPolicy = CustomerPolicy.getCustomerPolicy(MemberTest.청소년_멤버);
        CustomerPolicy childPolicy = CustomerPolicy.getCustomerPolicy(MemberTest.어린이_멤버);

        Fare fare = new Fare(2050);
        assertThat(adultPolicy.apply(fare).getValue()).isEqualTo(2050);
        assertThat(teenagerPolicy.apply(fare).getValue()).isEqualTo(1360);
        assertThat(childPolicy.apply(fare).getValue()).isEqualTo(850);
    }
}
