package nextstep.subway.path.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class DistanceFarePolicyTest {
	@DisplayName("거리별 추가 요금")
	@ParameterizedTest
	@CsvSource({"10,1250", "15,1350", "16,1450", "58,2150", "59,2250"})
	void calculateFare(int distance, int fare) {
		assertThat(DistanceFarePolicy.calculateFare(distance)).isEqualTo(fare);
	}
}
