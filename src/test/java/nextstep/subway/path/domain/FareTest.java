package nextstep.subway.path.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class FareTest {
	@ParameterizedTest
	@CsvSource({"10,1250", "15,1350", "16,1450", "58,2150", "59,2250"})
	void getFare(int distance, int fare) {
		assertThat(new Fare(distance).value()).isEqualTo(fare);
	}
}
