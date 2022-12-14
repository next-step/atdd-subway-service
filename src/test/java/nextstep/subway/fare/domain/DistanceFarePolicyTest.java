package nextstep.subway.fare.domain;

import nextstep.subway.line.domain.Distance;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class DistanceFarePolicyTest {

    @DisplayName("주어진 거리에 해당되는 초과 요금 정책 목록을 반환한다")
    @ParameterizedTest(name = "[{0}km] 거리가 주어지면 [{1}] 초과 요금 정책 목록을 반환한다")
    @MethodSource("provideDistanceAndOverFarePolicy")
    void find_over_fare_policy_with_distance(int distance, DistanceFarePolicy expectedFarePolicy) {
        // when
        DistanceFarePolicy distanceFarePolicy = DistanceFarePolicy.of(new Distance(distance));

        // then
        assertThat(distanceFarePolicy).isEqualTo(expectedFarePolicy);
    }

    private static Stream<Arguments> provideDistanceAndOverFarePolicy() {
        return Stream.of(
                Arguments.of(10, DistanceFarePolicy.NONE),
                Arguments.of(17, DistanceFarePolicy.OVER_TEN),
                Arguments.of(50, DistanceFarePolicy.OVER_TEN),
                Arguments.of(57, DistanceFarePolicy.OVER_FIFTY)
        );
    }

    @DisplayName("주어진 거리에 따른 초과 요금을 계산하여 반환한다")
    @ParameterizedTest(name = "[{0}km] 거리가 주어지면 초과 요금 정책으로 계산 된 추가 요금[{1}원] 을 반환한다")
    @MethodSource("provideDistanceAndOverFare")
    void calculate_over_fare(int distance, Fare expectedFare) {
        // given
        Distance distanceWrapper = new Distance(distance);
        DistanceFarePolicy distanceFarePolicy = DistanceFarePolicy.of(distanceWrapper);

        // when
        Fare fare = distanceFarePolicy.apply(distanceWrapper);

        // then
        assertThat(fare).isEqualTo(expectedFare);
    }

    private static Stream<Arguments> provideDistanceAndOverFare() {
        return Stream.of(
                Arguments.of(10, Fare.zero()),
                Arguments.of(17, Fare.of(200)),
                Arguments.of(50, Fare.of(800)),
                Arguments.of(57, Fare.of(900))
        );
    }
}
