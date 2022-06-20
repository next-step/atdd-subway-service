package nextstep.subway.fare.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class FareTest {

    @ParameterizedTest(name = "{0}km 구간의 요금은 {1}원이다")
    @CsvSource(value = {"10:1250", "50:2050", "58:2150", "100:2750"}, delimiter = ':')
    void calculateFare(int distance, int expected) {
        // given & when
        int actual = Fare.calculateFare(distance);

        // then
        assertThat(actual).isEqualTo(expected);
    }
}
