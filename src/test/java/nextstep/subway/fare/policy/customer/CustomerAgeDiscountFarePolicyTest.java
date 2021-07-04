package nextstep.subway.fare.policy.customer;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import nextstep.subway.fare.domain.Fare;
import nextstep.subway.fare.policy.FarePolicy;
import nextstep.subway.member.MemberTest;

@DisplayName("요금 정책 단위 테스트")
@ExtendWith(MockitoExtension.class)
class CustomerAgeDiscountFarePolicyTest {
    @Test
    @DisplayName("고객_할인적용_검증_테스트")
    void 고객_할인적용_검증_테스트() {
        FarePolicy adultPolicy = CustomerType.getPolicy(MemberTest.일반_멤버);
        FarePolicy teenagerPolicy = CustomerType.getPolicy(MemberTest.청소년_멤버);
        FarePolicy childPolicy = CustomerType.getPolicy(MemberTest.어린이_멤버);

        Fare fare = Fare.DEFAULT;
        assertThat(fare.apply(adultPolicy).getValue()).isEqualTo(1250);
        assertThat(fare.apply(teenagerPolicy).getValue()).isEqualTo(720);
        assertThat(fare.apply(childPolicy).getValue()).isEqualTo(450);
    }
}
