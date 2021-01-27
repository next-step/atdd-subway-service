package nextstep.subway.line.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

@DisplayName("나이에 따른 지하철 요금 테스트")
public class AgeFarePolicyTest {

	@DisplayName("어린이 지하철 요금 테스트")
	@ParameterizedTest(name = "{index} => distance={0}, expectedFare={1}")
	@CsvSource(value = { "1:450", "9:450", "11:500", "15:500", "45:800", "49:850", "66:950", "178:1650"},
		delimiter = ':')
	void ageFareTestWithChild(int distance, int expected) {
		final int age = 11;
		Fare distanceFare = DistanceFarePolicy.calculateDistanceFare(distance);
		Fare result = AgeFarePolicy.calculate(age, distanceFare);
		assertThat(result.getFare()).isEqualTo(expected);
	}

	@DisplayName("청소 지하철 요금 테스트")
	@ParameterizedTest(name = "{index} => distance={0}, expectedFare={1}")
	@CsvSource(value = { "1:720", "9:720", "11:800", "15:800", "45:1280", "49:1360", "66:1520", "178:2640"},
		delimiter = ':')
	void ageFareTestWithStudent(int distance, int expected) {
		final int age = 18;
		Fare distanceFare = DistanceFarePolicy.calculateDistanceFare(distance);
		Fare result = AgeFarePolicy.calculate(age, distanceFare);
		assertThat(result.getFare()).isEqualTo(expected);
	}

}
