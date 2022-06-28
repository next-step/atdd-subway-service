package nextstep.subway.fare.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class AgeFarePolicyTest {
    @Test
    @DisplayName("키즈 요금 할인")
    void kidsPolicy() {
        // when
        final int fare = AgeFarePolicy.ofDiscount(8, 1250);
        // then
        assertThat(fare).isEqualTo(450);
    }

    @Test
    @DisplayName("청소년 요금 할인")
    void teenagerPolicy() {
        // when
        final int fare = AgeFarePolicy.ofDiscount(18, 1250);
        // then
        assertThat(fare).isEqualTo(720);
    }

    @Test
    @DisplayName("성인 요금 할인")
    void adultPolict() {
        // when
        final int fare = AgeFarePolicy.ofDiscount(20, 1250);
        // then
        assertThat(fare).isEqualTo(1250);
    }
}
