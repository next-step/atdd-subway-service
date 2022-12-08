package nextstep.subway.line.domain;

import nextstep.subway.line.exception.InvalidDistanceException;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class DistanceTest {

    @ParameterizedTest
    @CsvSource({"10,1,9", "2,1,1", "100,1,99"})
    void minus(int value, int toSubtract, int expectedResult) {
        Distance distance = Distance.valueOf(value);
        Distance subtracand = Distance.valueOf(toSubtract);
        assertThat(distance.minus(subtracand)).isEqualTo(Distance.valueOf(expectedResult));
    }

    @ParameterizedTest
    @CsvSource({"10,1,11", "2,1,3", "100,1,101"})
    void add(int value, int toAdd, int expectedResult) {
        Distance distance = Distance.valueOf(value);
        Distance addend = Distance.valueOf(toAdd);
        assertThat(distance.add(addend)).isEqualTo(Distance.valueOf(expectedResult));
    }

    @ParameterizedTest
    @CsvSource({"10,1,10", "3,2,1", "100,2,50"})
    void divide(int value, int divideBy, int expectedResult) {
        Distance distance = Distance.valueOf(value);
        Distance dividend = Distance.valueOf(divideBy);
        assertThat(distance.divide(dividend)).isEqualTo(Distance.valueOf(expectedResult));
    }

    @ParameterizedTest
    @CsvSource({"1,2,true", "2,1,false", "10,10,false"})
    void isLessThan(int value, int compare, boolean result) {
        Distance distance = Distance.valueOf(value);
        Distance compareDistance = Distance.valueOf(compare);
        assertThat(distance.isLessThan(compareDistance)).isEqualTo(result);
    }

    @ParameterizedTest
    @CsvSource({"1,2,true", "2,1,false", "10,10,true"})
    void isLessThanOrEqualTo(int value, int compare, boolean result) {
        Distance distance = Distance.valueOf(value);
        Distance compareDistance = Distance.valueOf(compare);
        assertThat(distance.isLessThanOrEqualTo(compareDistance)).isEqualTo(result);
    }

    @ParameterizedTest
    @ValueSource(ints = {0, -1, Integer.MIN_VALUE})
    void distanceCannotBeLessThanZero(int value) {
        assertThatThrownBy(() -> Distance.valueOf(value))
                .isInstanceOf(InvalidDistanceException.class)
                .hasMessage(InvalidDistanceException.LESS_THAN_ZERO);
    }
}
