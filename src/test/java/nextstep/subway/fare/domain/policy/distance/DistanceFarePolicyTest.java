package nextstep.subway.fare.domain.policy.distance;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.stream.Stream;
import nextstep.subway.fare.domain.policy.age.impl.AdultAgeFarePolicy;
import nextstep.subway.fare.domain.policy.distance.impl.DefaultDistanceFarePolicy;
import nextstep.subway.fare.domain.policy.distance.impl.LongDistanceFarePolicy;
import nextstep.subway.fare.domain.policy.distance.impl.MiddleDistanceFarePolicy;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class DistanceFarePolicyTest {

    @ParameterizedTest(name = "거리가 {0}KM 일 경우")
    @DisplayName("거리에 따른 요금정책 클래스를 리턴한다.")
    @MethodSource("providerGetDistanceFarePolicy_successCase")
    void getDistanceFarePolicy(int distance, Class<? extends DistanceFarePolicy> distanceFarePolicyClass) {
        DistanceFarePolicy distanceFarePolicy = DistanceFarePolicyType.getDistanceFarePolicy(distance);
        assertThat(distanceFarePolicy).isInstanceOf(distanceFarePolicyClass);
    }

    @ParameterizedTest(name = "{0}KM 일 경우, 추가 요금은 {2}원 이다.")
    @DisplayName("성인 기준, 거리에 따른 추가 요금을 계산한다.")
    @MethodSource("providerCalculate_successCase")
    void calculate(int distance, DistanceFarePolicy distanceFarePolicy, int expectedFare) {
        assertThat(distanceFarePolicy.calculate(distance, new AdultAgeFarePolicy())).isEqualTo(expectedFare);
    }

    private static Stream<Arguments> providerGetDistanceFarePolicy_successCase() {
        return Stream.of(
            Arguments.of(5, DefaultDistanceFarePolicy.class),
            Arguments.of(25, MiddleDistanceFarePolicy.class),
            Arguments.of(70, LongDistanceFarePolicy.class)
        );
    }

    private static Stream<Arguments> providerCalculate_successCase() {
        return Stream.of(
            Arguments.of(5, new DefaultDistanceFarePolicy(), 0),
            Arguments.of(25, new MiddleDistanceFarePolicy(), 300),
            Arguments.of(70, new LongDistanceFarePolicy(), 1100)
        );
    }

}
