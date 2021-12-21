package nextstep.subway.path.domain.fare;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class DistanceFarePolicyTest {

    @DisplayName("10 ~ 50km 요금 계산")
    @ParameterizedTest
    @CsvSource(value = {"9,0", "10,0", "11,100", "50,800", "51,800"})
    void tenToFiftyCalculateFare(int distance, int fare) {
        assertThat(DistanceFarePolicy.TEN_TO_FIFTY.calculateFare(distance)).isEqualTo(Fare.from(fare));
    }

    @DisplayName("50km 이상 요금 계산")
    @ParameterizedTest
    @CsvSource(value = {"49,0", "50,0", "51,100", "58,100"})
    void overFiftyCalculateFare(int distance, int fare) {
        assertThat(DistanceFarePolicy.OVER_FIFTY.calculateFare(distance)).isEqualTo(Fare.from(fare));
    }
}