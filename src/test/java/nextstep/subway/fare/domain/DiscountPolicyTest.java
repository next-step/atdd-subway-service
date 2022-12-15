package nextstep.subway.fare.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("연령별 할인된 결과 요금 계산")
class DiscountPolicyTest {
    @Test
    @DisplayName("어른은 할인이 없음")
    void applyDiscountAsAdult() {
        DiscountPolicy adult = DiscountPolicy.ADULT;
        int discounted = adult.getDiscount(2500);

        assertThat(discounted).isEqualTo(2500);
    }

    @Test
    @DisplayName("청소년은 (요금 - 350) * 0.8이 최종 요금이 됨 ")
    void applyDiscountByTeenAger() {
        DiscountPolicy teenager = DiscountPolicy.TEENAGER;
        int discounted = teenager.getDiscount(2350);

        assertThat(discounted).isEqualTo(1600);
    }

    @Test
    @DisplayName("어린이는 (요금 - 350) * 0.5가 최종 요금이 됨 ")
    void applyDiscountByChild() {
        DiscountPolicy child = DiscountPolicy.CHILD;
        int discounted = child.getDiscount(2350);

        assertThat(discounted).isEqualTo(1000);
    }

    @Test
    @DisplayName("어린이보다 어리면 요금 * 0이 최종 요금이 됨 ")
    void applyDiscountByInfant() {
        DiscountPolicy child = DiscountPolicy.INFANT;
        int discounted = child.getDiscount(2350);

        assertThat(discounted).isEqualTo(0);
    }
}
