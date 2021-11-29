package nextstep.subway.fare.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

class DiscountRateTypeTest {

    @DisplayName("나이대 별 할인이 적용된 요금을 계산한다.")
    @ParameterizedTest
    @CsvSource(value = {"1,0", "5,0", "6,325", "12,325", "13,520", "18,520", "19,1000", "20,1000"})
    void discount1(int age, int expectedFare) {
        // given
        int fare = 1000;

        // when
        int actualFare = DiscountRateType.discountBy(fare, age);

        // then
        assertThat(actualFare).isEqualTo(expectedFare);
    }

    @DisplayName("할인을 적용하여 요금을 계산할 때, 나이가 0 이하이면 예외가 발생한다.")
    @ParameterizedTest
    @ValueSource(ints = {0, -1, -2, -5, -10})
    void discount2(int age) {
        // given
        int fare = 1000;

        // when & then
        assertThatIllegalArgumentException().isThrownBy(() -> DiscountRateType.discountBy(fare, age));
    }
}