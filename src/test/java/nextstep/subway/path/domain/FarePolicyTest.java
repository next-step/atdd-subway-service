package nextstep.subway.path.domain;

import static nextstep.subway.line.domain.LineFixture.*;
import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

import nextstep.subway.line.domain.Line;

@DisplayName("요금 정책")
class FarePolicyTest {

	@DisplayName("거리별 요금 정책을 계산한다.")
	@ParameterizedTest
	@CsvSource(value = {
		"5,1250",
		"10,1250",
		"11,1350",
		"12,1350",
		"15,1350",
		"16,1450",
		"25,1550",
		"50,2050",
		"51,2150",
		"55,2150",
		"58,2150",
		"59,2250"
	})
	void calculateByDistance(int distance, int expectedFare) {
		// given
		FarePolicy farePolicy = new FarePolicy();

		// when
		int actualFare = farePolicy.calculateBy(distance);

		// then
		assertThat(actualFare).isEqualTo(expectedFare);
	}

	@DisplayName("노선별 추가 요금 정책이 포함된 거리별 요금 정책을 계산한다.")
	@ParameterizedTest
	@MethodSource("calculateByDistanceAndLinesMethodSource")
	void calculateByDistanceAndLines(int distance, List<Line> lines, int expectedFare) {
		// given
		FarePolicy farePolicy = new FarePolicy();

		// when
		int actualFare = farePolicy.calculateBy(distance, lines);

		// then
		assertThat(actualFare).isEqualTo(expectedFare);
	}

	private static List<Arguments> calculateByDistanceAndLinesMethodSource() {
		return Arrays.asList(
			Arguments.of(
				10,
				Arrays.asList(신분당선(), 이호선()),
				1250 + 900
			),
			Arguments.of(
				10,
				Arrays.asList(이호선(), 삼호선()),
				1250 + 500
			),
			Arguments.of(
				10,
				Arrays.asList(삼호선()),
				1250
			));
	}
}
