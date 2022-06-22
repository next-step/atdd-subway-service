package nextstep.subway.line.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class DiscountTest {
    private Discount thirtyPerCent;

    @BeforeEach
    void setUp() {
        thirtyPerCent = new Discount(30);
    }

    @DisplayName("할인률을 가진다.")
    @Test
    void createTest() {
        assertThat(thirtyPerCent).isEqualTo(new Discount(30));
    }

    @DisplayName("두 Discount 를 더하면 총합의 Discount 를 반환한다.")
    @Test
    void plusTest() {
        assertThat(thirtyPerCent.plus(new Discount(20))).isEqualTo(new Discount(50));
    }

    @DisplayName("Discount 는 음수를 가질수 없다.")
    @Test
    void invalidCreateTest() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> new Discount(-1));
    }

    @DisplayName("기존 Discount 에서 새로운 Discount 를 뺄수 있다.")
    @Test
    void minusTest() {
        assertThat(thirtyPerCent.minus(new Discount(10))).isEqualTo(new Discount(20));
    }

    /**
     * Given : 결재가 예상되는 금액이 제공되고
     * When : Discount 에 해당 금액을 입력하면
     * Then : 제공된 할인 금액을 제외하고 나머지 금액을 반환한다.
     */
    @DisplayName("Discount 에 금액을 입력하면 할인률이 적용된 후 나머지 금액을 반환한다.")
    @Test
    void calculatorTest() {
        final Charge discountedCharge =  thirtyPerCent.calculate(new Charge(1000));
    }

}