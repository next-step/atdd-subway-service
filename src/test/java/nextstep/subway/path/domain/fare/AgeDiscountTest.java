package nextstep.subway.path.domain.fare;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class AgeDiscountTest {

    @DisplayName("어린이일 경우 요금 계산")
    @Test
    void discountWhenChild() {
        AgeDiscount ageDiscount = AgeDiscount.findAgeDiscount(6);

        int result = ageDiscount.discountFare(3000);

        assertThat(result).isEqualTo(1325);
    }

    @DisplayName("청소년일 경우 요금 계산")
    @Test
    void discountWhenTeen() {
        AgeDiscount ageDiscount = AgeDiscount.findAgeDiscount(13);

        int result = ageDiscount.discountFare(3000);

        assertThat(result).isEqualTo(2120);
    }

    @DisplayName("어린이, 청소년 모두 해당 안 될 경우")
    @Test
    void discountWhenNone() {
        AgeDiscount ageDiscount = AgeDiscount.findAgeDiscount(19);

        int result = ageDiscount.discountFare(3000);

        assertThat(result).isEqualTo(3000);
    }
}
