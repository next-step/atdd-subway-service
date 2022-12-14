package nextstep.subway.fare.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class DiscountPolicyTest {
    @Test
    void applyDiscountAsAdult() {
        DiscountPolicy adult = DiscountPolicy.ADULT;
        int discounted = adult.discount(2500);

        assertThat(discounted).isEqualTo(2500);
    }

    @Test
    void applyDiscountByTeenAger() {
        DiscountPolicy teenager = DiscountPolicy.TEENAGER;
        int discounted = teenager.discount(2350);

        assertThat(discounted).isEqualTo(1600);
    }

    @Test
    void applyDiscountByChild() {
        DiscountPolicy child = DiscountPolicy.CHILD;
        int discounted = child.discount(2350);

        assertThat(discounted).isEqualTo(1000);
    }

    @Test
    void applyDiscountByInfant() {
        DiscountPolicy child = DiscountPolicy.INFANT;
        int discounted = child.discount(2350);

        assertThat(discounted).isEqualTo(0);
    }
}
