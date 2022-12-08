package nextstep.subway.path.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static nextstep.subway.path.domain.Fare.DISCOUNT_RATE_CHILD;
import static nextstep.subway.path.domain.Fare.DISCOUNT_RATE_TEEN;
import static org.assertj.core.api.Assertions.assertThat;

class FareTest {

    @DisplayName("어린이는 운임에서 350원을 공제한 금액의 50% 할인이 적용된다")
    @Test
    void applyDiscountFare_child() {
        Fare fare = new Fare(1000);

        fare.applyDiscountFare(DISCOUNT_RATE_CHILD);

        assertThat(fare).isEqualTo(new Fare(675));
    }

    @DisplayName("청소년은 운임에서 350원을 공제한 금액의 20% 할인이 적용된다")
    @Test
    void applyDiscountFare_teen() {
        Fare fare = new Fare(1000);

        fare.applyDiscountFare(DISCOUNT_RATE_TEEN);

        assertThat(fare).isEqualTo(new Fare(870));
    }
}
