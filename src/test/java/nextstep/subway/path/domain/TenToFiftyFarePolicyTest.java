package nextstep.subway.path.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class TenToFiftyFarePolicyTest {

    @DisplayName("10 ~ 50km 요금 계산")
    @ParameterizedTest
    @CsvSource(value = {"9,0", "10,0", "11,100", "50,800", "51,800"})
    void calculateFare(int distance, int fare) {
        TenToFiftyFarePolicy tenToFiftyFarePolicy = new TenToFiftyFarePolicy();

        assertThat(tenToFiftyFarePolicy.calculateFare(distance)).isEqualTo(Fare.from(fare));
    }
}