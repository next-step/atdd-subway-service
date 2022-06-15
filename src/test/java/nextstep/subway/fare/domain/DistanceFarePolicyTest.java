package nextstep.subway.fare.domain;

import static org.assertj.core.api.Assertions.assertThat;

import nextstep.subway.line.domain.Distance;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class DistanceFarePolicyTest {
    @DisplayName("거리별 거리 요금 정책 조회 테스트")
    @ParameterizedTest(name = "거리가 {0}일 때 해당하는 거리 요금 정책 {1} 확인 테스트")
    @CsvSource(value = {"10, BASIC_POLICY", "30, FIRST_ADDITIONAL_POLICY", "51, SECOND_ADDITIONAL_POLICY"})
    void findDistanceFarePolicyByDistance(int input, DistanceFarePolicy distanceFarePolicy) {
        DistanceFarePolicy distanceFarePolicyByDistance = DistanceFarePolicy.findDistanceFarePolicyByDistance(
                Distance.valueOf(input));
        assertThat(distanceFarePolicyByDistance).isEqualTo(distanceFarePolicy);
    }

    @DisplayName("거리별 거리 요금 정책 요금 반환 테스트")
    @ParameterizedTest(name = "거리가 {0}일 때 해당하는 거리 요금 정책의 요금 {1} 반환 테스트")
    @CsvSource(value = {"10, 1250", "30, 1650", "51, 2150", "66, 2250"})
    void findDistanceFarePolicyByDistance(int input, int fare) {
        Distance distance = Distance.valueOf(input);
        DistanceFarePolicy distanceFarePolicyByDistance = DistanceFarePolicy.findDistanceFarePolicyByDistance(distance);
        assertThat(distanceFarePolicyByDistance.calculateFare(distance)).isEqualTo(fare);
    }
}
