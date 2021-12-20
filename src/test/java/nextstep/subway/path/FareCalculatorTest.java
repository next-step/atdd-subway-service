package nextstep.subway.path;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import nextstep.subway.path.domain.FareCalculator;

public class FareCalculatorTest {

	@ValueSource(ints = {0, 5, 8, 10})
	@DisplayName("10km 이하 경로 시, 기본 운임 적용")
	@ParameterizedTest
	void calculate(int distance) {

		int fare = FareCalculator.calculate(distance);

		assertThat(fare).isEqualTo(1250);
	}

	@CsvSource(value = {"11,1350", "15,1350", "16,1450", "20, 1450", "21,1550", "50,2050"})
	@DisplayName("10km 초과 ~ 50km 이하, 5km 까지마다 100원 추가 운임 적용")
	@ParameterizedTest
	void calculate2(int distance, int expectedFare) {

		int fare = FareCalculator.calculate(distance);

		assertThat(fare).isEqualTo(expectedFare);
	}

	@CsvSource(value = {"51,1850", "58,1850", "59,1950", "66,1950", "67,2050"})
	@DisplayName("50km 초과 시, 8km 까지마다 100원 추가 운임 적용")
	@ParameterizedTest
	void calculate3(int distance, int expectedFare) {

		int fare = FareCalculator.calculate(distance);

		assertThat(fare).isEqualTo(expectedFare);
	}

}
