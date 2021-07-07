package nextstep.subway.fare.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class FareTest {

    @ParameterizedTest
    @CsvSource(value = {"0,0,0", "0,100,0", "1,0,1250", "1,100,1350", "11,0,1350", "50,100,2150", "51,100,2250",
        "59,100,2350"})
    void given_distance_when_Calculate_then_ReturnFare(final int distance, final int extraFare, final long expected) {
        // given
        final Fare fare = new Fare(distance, extraFare);

        // when
        final long actual = fare.calculate();

        // then
        assertThat(actual).isEqualTo(expected);
    }
}
