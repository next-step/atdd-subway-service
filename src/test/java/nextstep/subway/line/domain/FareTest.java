package nextstep.subway.line.domain;

import static org.assertj.core.api.Assertions.assertThat;

import nextstep.subway.util.FareCalculator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class FareTest {

    @DisplayName("거리에 따른 지하철 요금을 계산한다.")
    @ParameterizedTest
    @CsvSource(value = {"5:1250", "12:1350", "22:1550", "52:2150", "56:2150", "59:2250"}, delimiter = ':')
    public void calculateFare(int distance, int fareResult) {
        Fare fare = Fare.of(0);
        assertThat(FareCalculator.calculateFare(distance, fare.value())).isEqualTo(Fare.of(fareResult));
    }
}
