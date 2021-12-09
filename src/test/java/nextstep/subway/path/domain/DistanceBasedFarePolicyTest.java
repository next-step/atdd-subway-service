package nextstep.subway.path.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

@DisplayName("거리별 요금 정책")
class DistanceBasedFarePolicyTest {
	private static final int BASIC_FARE = 1250;

	@DisplayName("추가 요금 구간별로 거리별 요금을 계산한다.")
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
	void calculatePerOverFareSection(int distance, int expectedFare) {
		// given
		DistanceBasedFarePolicy policy = new PerOverchargeFareSectionDistanceBasedFarePolicy();

		// when
		int actualFare = policy.calculate(BASIC_FARE, distance);

		// then
		assertThat(actualFare).isEqualTo(expectedFare);
	}
}
