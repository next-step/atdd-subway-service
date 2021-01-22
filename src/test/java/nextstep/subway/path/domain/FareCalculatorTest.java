package nextstep.subway.path.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static nextstep.subway.path.domain.FareCalculator.*;
import static org.assertj.core.api.Assertions.assertThat;

class FareCalculatorTest {

    @DisplayName("거리에 따른 요금 계산")
    @ParameterizedTest
    @CsvSource(value = {"1:1250", "10:1250", "20:1450", "50:2050", "58:2150", "59:2250"}, delimiter = ':')
    void calculateOf(int distance, int fare) {
        assertThat(FareCalculator.calculateFareOf(distance)).isEqualTo(fare);
    }

    @DisplayName("연령에 따라서 요금이 할인된다. - 어린이")
    @ParameterizedTest
    @ValueSource(ints = {6, 7, 8, 9, 10, 11, 12})
    void calculateDiscountedFareByAgeForChildren(int age) {
        // given
        int fare = FareCalculator.calculateFareOf(100);
        // when
        int discountedFareByAge = FareCalculator.calculateDiscountedFareByAge(fare, age);
        // then
        assertThat(discountedFareByAge).isEqualTo((int) ((fare - BASIC_DISCOUNT_FARE) * DISCOUNT_RATE_FOR_CHILDREN));
    }

    @DisplayName("연령에 따라서 요금이 할인된다. - 청소년")
    @ParameterizedTest
    @ValueSource(ints = {13, 14, 15, 16, 17, 18})
    void calculateDiscountedFareByAgeForYouth(int age) {
        // given
        int fare = FareCalculator.calculateFareOf(100);
        // when
        int discountedFareByAge = FareCalculator.calculateDiscountedFareByAge(fare, age);
        // then
        assertThat(discountedFareByAge).isEqualTo((int) ((fare - BASIC_DISCOUNT_FARE) * DISCOUNT_RATE_FOR_YOUTH));
    }
}
