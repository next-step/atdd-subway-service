package nextstep.subway.line.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

@DisplayName("거리에 따른 지하철 요금 테스트")
public class DistanceFarePolicyTest {

	@DisplayName("지하철 요금 테스트")
	@ParameterizedTest(name = "{index} => distance={0}, expectedFare={1}")
	@CsvSource(value = { "1:1250", "9:1250", "11:1350", "15:1350", "45:1950", "49:2050", "66:2250", "178:3650"},
		delimiter = ':')
	void fareTest(int distance, int expected) {
		Fare result = DistanceFarePolicy.calculateDistanceFare(distance);
		assertThat(result.getFare()).isEqualTo(expected);
	}

}
