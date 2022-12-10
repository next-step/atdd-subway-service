package nextstep.subway.path.unit.fare;

import static org.junit.jupiter.api.Assertions.assertEquals;

import nextstep.subway.path.domain.fare.Discount;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class DiscountTest {

    @DisplayName("아동 할인률(50%)을 조회할 수 있다.")
    @Test
    void getDiscountByAge_child_test() {
        // given
        int 아동_나이 = 10;
        // when
        Discount discount = Discount.getDiscountByAge(아동_나이);
        // then
        assertEquals(Discount.CHILD_DISCOUNT, discount);
        assertEquals(0.5, discount.getRate());
    }

    @DisplayName("청소년 할인률(20%)을 조회할 수 있다.")
    @Test
    void getDiscountByAge_teenager_test() {
        // given
        int 청소년_나이 = 17;
        // when
        Discount discount = Discount.getDiscountByAge(청소년_나이);
        // then
        assertEquals(Discount.TEENAGER_DISCOUNT, discount);
        assertEquals(0.2, discount.getRate());
    }

}
