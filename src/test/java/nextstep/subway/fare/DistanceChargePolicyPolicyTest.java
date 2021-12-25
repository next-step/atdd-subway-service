package nextstep.subway.fare;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import nextstep.subway.fare.domain.DistanceChargePolicy;
import nextstep.subway.fare.domain.Fare;

public class DistanceChargePolicyPolicyTest {

	@ValueSource(ints = {0, 5, 8, 10})
	@DisplayName("10km 이하 경로 시, 기본 운임 적용")
	@ParameterizedTest
	void calculate(int distance) {

		Fare fare = DistanceChargePolicy.getFare(distance);

		assertThat(fare.isZero()).isTrue();
	}

	@CsvSource(value = {"11,100", "15,100", "16,200", "20, 200", "21,300", "26,400", "50,800"})
	@DisplayName("10km 초과 ~ 50km 이하, 5km 까지마다 100원 추가 운임 적용")
	@ParameterizedTest
	void calculate2(int distance, int expectedFare) {

		Fare fare = DistanceChargePolicy.getFare(distance);

		assertThat(fare).isEqualTo(Fare.of(expectedFare));
	}

	@CsvSource(value = {"51,900", "58,900", "59,1000", "66,1000", "67,1100"})
	@DisplayName("50km 초과 시, 8km 까지마다 100원 추가 운임 적용")
	@ParameterizedTest
	void calculate3(int distance, int expectedFare) {

		Fare fare = DistanceChargePolicy.getFare(distance);

		assertThat(fare).isEqualTo(Fare.of(expectedFare));
	}

}
