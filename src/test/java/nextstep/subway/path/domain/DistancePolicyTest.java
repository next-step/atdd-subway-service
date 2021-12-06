package nextstep.subway.path.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.*;

@DisplayName("거리별 요금 정책 테스트")
class DistancePolicyTest {

    @ParameterizedTest(name = "{displayName} - [{index}] {argumentsWithNames}")
    @CsvSource(value = {"MEDIUM:50:800", "LONG:65:200"}, delimiter = ':')
    @DisplayName("거리별 요금을 반환환다.")
    void calculateFare(DistancePolicy distancePolicy, int distance, int expectedFare) {
        // when
        int fare = distancePolicy.calculateFare(distance);

        // then
        assertThat(fare).isEqualTo(expectedFare);
    }
}
