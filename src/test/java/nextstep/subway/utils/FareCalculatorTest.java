package nextstep.subway.utils;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import nextstep.subway.fare.domain.Fare;
import nextstep.subway.line.domain.Distance;

public class FareCalculatorTest {

	@DisplayName("요금 계산 테스트")
	@Test
	void 계산() {
		Fare fare = FareCalculator.getSubwayFare(new Distance(5), new Fare(1000), 20);
		assertThat(fare.value()).isEqualTo(2250);
		fare = FareCalculator.getSubwayFare(new Distance(15), new Fare(1000), 20);
		assertThat(fare.value()).isEqualTo(2550);
		fare = FareCalculator.getSubwayFare(new Distance(55), new Fare(1000), 20);
		assertThat(fare.value()).isEqualTo(2950);
		fare = FareCalculator.getSubwayFare(new Distance(55), new Fare(1000), 10);
		assertThat(fare.value()).isEqualTo(1650);
		fare = FareCalculator.getSubwayFare(new Distance(55), new Fare(1000), 15);
		assertThat(fare.value()).isEqualTo(2430);
	}

}
