package nextstep.subway.fare;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class FareDiscountTest {

    @DisplayName("13세 이상 19세 미만인 청소년은 20% 할인")
    @ParameterizedTest(name = "{0}세는 20% 할인")
    @ValueSource(ints = {13, 15, 18})
    void 나이별_할인율_청소년(int age) {
        // when
        double result = FareDiscount.getDiscountRateByAge(age);

        // then
        assertThat(result).isEqualTo(0.2);
    }

    @DisplayName("6세 이상 13세 미만인 어린이는 50% 할인")
    @ParameterizedTest(name = "{0}세는 50% 할인")
    @ValueSource(ints = {6, 10, 12})
    void 나이별_할인율_어린이(int age) {
        // when
        double result = FareDiscount.getDiscountRateByAge(age);

        // then
        assertThat(result).isEqualTo(0.5);
    }

    @DisplayName("청소년, 어린이에 속하지 않으면 할인 없음")
    @ParameterizedTest(name = "{0}세는 할인 없음")
    @ValueSource(ints = {1, 5, 19, 100})
    void 청소년_어린이가_아니면_할인_없음(int age) {
        // when
        double result = FareDiscount.getDiscountRateByAge(age);

        // then
        assertThat(result).isEqualTo(0);
    }

}