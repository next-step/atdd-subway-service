package nextstep.subway.path.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class OverFiftyFarePolicyTest {

    @DisplayName("50km 이상 요금 계산")
    @ParameterizedTest
    @CsvSource(value = {"49,0", "50,0", "51,100", "58,100"})
    void calculateFare(int distance, int fare) {
        OverFiftyFarePolicy overFiftyFarePolicy = new OverFiftyFarePolicy();

        assertThat(overFiftyFarePolicy.calculateFare(distance)).isEqualTo(Fare.from(fare));
    }
}