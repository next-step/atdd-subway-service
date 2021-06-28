package nextstep.subway.path.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class DistanceFareTest {

	@DisplayName("거리에 따른 요금 계산 테스트 - 1~10km")
	@Test
	void testDefault() {
		int distance = 8;
		DistanceFare distanceFare = DistanceFare.findByDistance(distance);
		int fare = distanceFare.calculateFare(distance);

		Assertions.assertThat(distanceFare).isEqualTo(DistanceFare.DEFAULT);
		Assertions.assertThat(fare).isEqualTo(1250);
	}

	@DisplayName("거리에 따른 요금 계산 테스트 - 11~50km")
	@Test
	void testFirstExtraCharge() {
		int distance = 15;
		DistanceFare distanceFare = DistanceFare.findByDistance(distance);
		int fare = distanceFare.calculateFare(distance);

		Assertions.assertThat(distanceFare).isEqualTo(DistanceFare.FIRST_EXTRA);
		Assertions.assertThat(fare).isEqualTo(1350);
	}

	@DisplayName("거리에 따른 요금 계산 테스트 - 51 ~ 10000km")
	@Test
	void testSecondExtraCharge() {
		int distance = 60;
		DistanceFare distanceFare = DistanceFare.findByDistance(distance);
		int fare = distanceFare.calculateFare(distance);

		Assertions.assertThat(distanceFare).isEqualTo(DistanceFare.SECOND_EXTRA);
		Assertions.assertThat(fare).isEqualTo(2250);
	}
}