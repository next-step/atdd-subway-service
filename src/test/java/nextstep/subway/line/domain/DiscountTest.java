package nextstep.subway.line.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

class DiscountTest {

    @DisplayName("age 값으로 Discount Enum 을 가져온다")
    @ParameterizedTest
    @CsvSource({
            "1, FREE",
            "6, CHILD", "12, CHILD",
            "13, TEENAGER", "18, TEENAGER",
            "19, ADULT",
            "1000, ADULT"})
    void of(int age, Discount discount) {
        assertThat(Discount.of(age)).isEqualTo(discount);
    }

    @DisplayName("연령대별 할인된 값을 계산한다.")
    @ParameterizedTest
    @CsvSource({
            "FREE, 2000, 0",
            "CHILD, 2000, 825",
            "TEENAGER, 2000, 1320",
            "ADULT, 2000, 2000",
            "ADULT, 2000, 2000"})
    void calculate(Discount discount, int fare, int expected) {
        assertThat(discount.calculate(fare)).isEqualTo(expected);
    }
}
