package nextstep.subway.line.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class FareTest {

    @ParameterizedTest
    @CsvSource(value = {"5:1250", "12:1350", "22:1550", "52:2150", "56:2150", "59:2250"}, delimiter = ':')
    public void calculateFare(int distance, int fareResult) {
        Fare fare = new Fare(0);
        assertThat(fare.calculateFare(distance)).isEqualTo(fareResult);
    }
}
