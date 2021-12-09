package nextstep.subway.path.domain;

import static nextstep.subway.line.domain.LineFixture.*;
import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import nextstep.subway.line.domain.Line;

@DisplayName("노선별 추가 요금 정책")
class LineOverchargeFarePolicyTest {

	@DisplayName("추가 요금이 있는 노선들 중 가장 비싼 노선의 추가 요금을 더한다.")
	@ParameterizedTest
	@MethodSource("overchargeMostExpensiveLineMethodSource")
	void overchargeMostExpensiveLine(int fare, List<Line> lines, int expectedFare) {
		// given
		LineOverchargeFarePolicy policy = new MostExpensiveLineOverchargeFarePolicy();

		// when
		int actualFare = policy.overcharge(fare, lines);

		// then
		assertThat(actualFare).isEqualTo(expectedFare);
	}

	private static List<Arguments> overchargeMostExpensiveLineMethodSource() {
		return Arrays.asList(
			Arguments.of(
				1250,
				Arrays.asList(신분당선(), 이호선()),
				1250 + 900
			),
			Arguments.of(
				1250,
				Arrays.asList(이호선(), 삼호선()),
				1250 + 500
			),
			Arguments.of(
				1250,
				Collections.singletonList(삼호선()),
				1250
			));
	}
}
