package nextstep.subway.path.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

class DistanceFarePolicyTest {
    @ParameterizedTest
    @DisplayName("요금 거리가 60, 30, 10일때 일반 성인기준 2250, 1650, 1250")
    @CsvSource(value = {"10:1250", "30:1650", "60:2250"}, delimiter = ':')
    void calculateFare(int distance, int expectedFare) {
        int fare = DistanceFarePolicy.getPolicy(distance).calculateFare(distance);
        assertThat(expectedFare).isEqualTo(fare);
    }
}