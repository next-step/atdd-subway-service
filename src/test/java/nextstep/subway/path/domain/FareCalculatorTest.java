package nextstep.subway.path.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static nextstep.subway.path.domain.Fare.DISCOUNT_FARE;
import static nextstep.subway.path.domain.FareAge.CHILDREN;
import static nextstep.subway.path.domain.FareAge.YOUTH;
import static org.assertj.core.api.Assertions.assertThat;

class FareCalculatorTest {

    @DisplayName("거리에 따른 요금 계산")
    @ParameterizedTest
    @CsvSource(value = {"1:1250", "10:1250", "20:1450", "50:2050", "58:2150", "59:2250"}, delimiter = ':')
    void calculateOf(int distance, int fare) {
        assertThat(FareSection.calculateFareOf(distance)).isEqualTo(fare);
    }

    @DisplayName("연령에 따라서 요금이 할인된다. - 어린이")
    @ParameterizedTest
    @ValueSource(ints = {6, 7, 8, 9, 10, 11, 12})
    void calculateDiscountedFareByAgeForChildren(int age) {
        // given
        int fare = FareSection.calculateFareOf(100);
        // when
        int discountedFareByAge = FareAge.calculateDiscountedFare(fare, age);
        // then
        int expected = (int) ((fare - DISCOUNT_FARE.getValue()) * CHILDREN.getDiscountRate());
        assertThat(discountedFareByAge).isEqualTo(expected);
    }

    @DisplayName("연령에 따라서 요금이 할인된다. - 청소년")
    @ParameterizedTest
    @ValueSource(ints = {13, 14, 15, 16, 17, 18})
    void calculateDiscountedFareByAgeForYouth(int age) {
        // given
        int fare = FareSection.calculateFareOf(100);
        // when
        int discountedFareByAge = FareAge.calculateDiscountedFare(fare, age);
        // then
        int expected = (int) ((fare - DISCOUNT_FARE.getValue()) * YOUTH.getDiscountRate());
        assertThat(discountedFareByAge).isEqualTo(expected);
    }
}
