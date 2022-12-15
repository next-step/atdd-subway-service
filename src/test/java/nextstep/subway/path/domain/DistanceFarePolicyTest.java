package nextstep.subway.path.domain;

import static org.assertj.core.api.Assertions.*;

import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import nextstep.subway.common.domain.Fare;
import nextstep.subway.line.domain.Distance;

@DisplayName("거리 할인 정책 테스트")
class DistanceFarePolicyTest {

	@DisplayName("거리 별 요금 계산")
	@ParameterizedTest(name = "[{index}] 거리: {0} | 요금: {1}")
	@MethodSource
	void calculateFarePerDistanceTest(Distance distance, Fare expected) {
		Fare fare = DistanceFarePolicy.calculate(distance);
		assertThat(fare).isEqualTo(expected);
	}

	private static Stream<Arguments> calculateFarePerDistanceTest() {
		return Stream.of(
			Arguments.of(Distance.from(1), Fare.from(1250)),
			Arguments.of(Distance.from(10), Fare.from(1250)),
			Arguments.of(Distance.from(30), Fare.from(1650)),
			Arguments.of(Distance.from(50), Fare.from(2050)),
			Arguments.of(Distance.from(100), Fare.from(2750))
		);
	}

}