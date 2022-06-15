package nextstep.subway.path.domain.policy;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class DiscountPolicyTest {

    @Test
    @DisplayName("할인을 받지 못하는 경우")
    void policyTest1() {
        DiscountPolicy policy = new DiscountPolicy() {
            @Override
            public int discount(int fare) {
                return fare;
            }
        };
        assertThat(policy.discount(500)).isEqualTo(500);
    }

    @Test
    @DisplayName("청소년 할인을 받는 경우")
    void policyTest2() {
        DiscountPolicy policy = new DiscountPolicy() {
            @Override
            public int discount(int fare) {
                if (fare <= 350) return 0;
                return (int) Math.ceil((fare - 350)*0.8);
            }
        };

        assertThat(policy.discount(500)).isEqualTo(120);
        assertThat(policy.discount(350)).isEqualTo(0);
    }

    @Test
    @DisplayName("어린이 할인을 받는 경우")
    void policyTest3() {
        DiscountPolicy policy = new DiscountPolicy() {
            @Override
            public int discount(int fare) {
                if (fare <= 350) return 0;
                return (int) Math.ceil((fare - 350) * 0.5);
            }
        };
        assertThat(policy.discount(500)).isEqualTo(75);
    }

}
