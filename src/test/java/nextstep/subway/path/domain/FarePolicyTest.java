package nextstep.subway.path.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

@DisplayName("요금 정책")
class FarePolicyTest {

	@DisplayName("")
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
	void calculate(int distance, int expectedFare) {
		// given
		FarePolicy farePolicy = new FarePolicyByDistance();

		// when
		int actualFare = farePolicy.calculate(distance);

		// then
		assertThat(actualFare).isEqualTo(expectedFare);
	}
}
