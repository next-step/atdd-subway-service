package nextstep.subway.utils;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import nextstep.subway.fare.domain.Fare;

public class FareCalculatorTest {

	@DisplayName("요금 계산 테스트")
	@Test
	void 계산() {
		Fare fare = FareCalculator.getSubwayFare(5, 1000, 20);
		assertThat(fare.value()).isEqualTo(2250);
		fare = FareCalculator.getSubwayFare(15, 1000, 20);
		assertThat(fare.value()).isEqualTo(2550);
		fare = FareCalculator.getSubwayFare(55, 1000, 20);
		assertThat(fare.value()).isEqualTo(2950);

		fare = FareCalculator.getSubwayFare(55, 1000, 10);
		assertThat(fare.value()).isEqualTo(1300);

		fare = FareCalculator.getSubwayFare(55, 1000, 15);
		assertThat(fare.value()).isEqualTo(2080);
	}

}
