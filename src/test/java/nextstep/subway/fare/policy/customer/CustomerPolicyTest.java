package nextstep.subway.fare.policy.customer;

import static nextstep.subway.member.MemberTest.*;
import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import nextstep.subway.fare.domain.Fare;
import nextstep.subway.fare.policy.FarePolicy;

@DisplayName("요금 정책 단위 테스트")
@ExtendWith(MockitoExtension.class)
class CustomerPolicyTest {
    @Test
    @DisplayName("고객_할인적용_검증_테스트")
    void 고객_할인적용_검증_테스트() {
        FarePolicy adultPolicy = CustomerPolicyType.of(일반_멤버).getPolicy();
        FarePolicy teenagerPolicy = CustomerPolicyType.of(청소년_멤버).getPolicy();
        FarePolicy childPolicy = CustomerPolicyType.of(어린이_멤버).getPolicy();

        Fare fare = Fare.DEFAULT;
        assertThat(fare.apply(adultPolicy).getValue()).isEqualTo(1250);
        assertThat(fare.apply(teenagerPolicy).getValue()).isEqualTo(720);
        assertThat(fare.apply(childPolicy).getValue()).isEqualTo(450);
    }
}
