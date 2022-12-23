package nextstep.subway.fare.discount;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static nextstep.subway.fare.AgeDiscount.create;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("연령별 할인")
class AgeDiscountTest {

    @DisplayName("청소년 할인 / 운임에서 350원을 공제한 금액의 20%할인")
    @ParameterizedTest
    @CsvSource(value = {"17:350:0.8", "16:350:0.8", "18:350:0.8"}, delimiter = ':')
    void discount_teenager(int age, int deductionFare, double rate) {
        assertThat(create(age).getDeductionFare()).isEqualTo(deductionFare);
        assertThat(create(age).getRate()).isEqualTo(rate);
    }

    @DisplayName("어린이 할인 / 운임에서 350원을 공제한 금액의 50%할인")
    @ParameterizedTest
    @CsvSource(value = {"6:350:0.5", "12:350:0.5"}, delimiter = ':')
    void discount_children(int age, int deductionFare, double rate) {
        assertThat(create(age).getDeductionFare()).isEqualTo(deductionFare);
        assertThat(create(age).getRate()).isEqualTo(rate);
    }

    @DisplayName("할인 없음")
    @ParameterizedTest
    @CsvSource(value = {"19:0:1", "20:0:1"}, delimiter = ':')
    void discount_default(int age, int deductionFare, double rate) {
        assertThat(create(age).getDeductionFare()).isEqualTo(deductionFare);
        assertThat(create(age).getRate()).isEqualTo(rate);
    }
}
