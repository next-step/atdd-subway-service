package nextstep.subway.path.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class TotalFarePolicyTest {

    @DisplayName("전체 요금 계산")
    @ParameterizedTest
    @CsvSource(value = {"9,1250", "10,1250", "11,1350", "50, 2050", "51,2050"})
    void calculateFare(int distance, int fare) {
        TotalFarePolicy totalFarePolicy = new TotalFarePolicy();

        assertThat(totalFarePolicy.calculateFare(distance)).isEqualTo(Fare.from(fare));
    }
}