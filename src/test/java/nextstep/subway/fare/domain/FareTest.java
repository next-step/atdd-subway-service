package nextstep.subway.fare.domain;

import nextstep.subway.fare.exception.BelowZeroIntegerException;
import nextstep.subway.fare.exception.LessZeroOperationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@DisplayName("요금 Test")
class FareTest {

    public static final Fare BASIC_FARE = new Fare(100);

    @ValueSource(ints = {0, 100, 300, 500, 800})
    @ParameterizedTest
    void constructor_성공(int value) {
        assertDoesNotThrow(() -> new Fare(value));
    }

    @ValueSource(ints = {-100, -500, -800})
    @ParameterizedTest
    void constructor_예외(int value) {
        assertThatExceptionOfType(BelowZeroIntegerException.class)
                .isThrownBy(() -> new Fare(value));
    }

    @CsvSource(value = {"100:50:150", "100:30:130", "100:80:180"}, delimiterString = ":")
    @ParameterizedTest
    void add(int left, int right, BigDecimal expectedResultValue) {
        // given
        Fare leftFare = new Fare(left);
        Fare rightFare = new Fare(right);
        Fare expectedResult = new Fare(expectedResultValue);

        // when, then
        assertThat(leftFare.add(rightFare).get()).isEqualTo(expectedResult.get());
    }

    @CsvSource(value = {"100:50", "100:30", "100:80", "100:100"}, delimiterString = ":")
    @ParameterizedTest
    void subtract_성공(int left, int right) {
        // given
        Fare leftFare = new Fare(left);
        Fare rightFare = new Fare(right);

        // when, then
        assertDoesNotThrow(() -> leftFare.subtract(rightFare));
    }

    @CsvSource(value = {"100:101", "100:120", "100:150"}, delimiterString = ":")
    @ParameterizedTest
    void subtract_예외(int left, int right) {
        // given
        Fare leftFare = new Fare(left);
        Fare rightFare = new Fare(right);

        // when, then
        assertThatExceptionOfType(LessZeroOperationException.class)
                .isThrownBy(() -> leftFare.subtract(rightFare));
    }

    @CsvSource(value = {"100:0.2:20", "100:0.3:30", "100:0.5:50"}, delimiterString = ":")
    @ParameterizedTest
    void multiply_성공(int fareValue, BigDecimal rateValue, BigDecimal expectedResultValue) {
        // given
        Fare fare = new Fare(fareValue);
        Rate rate = new Rate(rateValue);
        Fare expectedResult = new Fare(expectedResultValue);

        // when, then
        assertThat(fare.multiply(rate).get()).isEqualTo(expectedResult.get());
    }
}