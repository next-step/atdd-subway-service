package nextstep.subway.path.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class SubwayFareTest {

	@ParameterizedTest
	@CsvSource(value = {"1:1250", "9:1250", "10:1250"
		, "11:1350", "15:1350", "16:1450", "20:1450", "26:1650", "50:2050"
		, "51:2150", "58:2150", "59:2250", "66:2250", "67:2350", "75:2450"
	}, delimiter = ':')
	@DisplayName("최단거리에 해당하는 요금이 계산되어야한다.")
	void calculateFare(int distance, int fare) {
		//given
		int distanceFare = SubwayFare.calculateDistanceFare(distance, 0);

		//when-then
		assertThat(distanceFare).isEqualTo(fare);
	}

	@Test
	@DisplayName("추가요금이 있는 노선을 이용 시, 그 중 최대 추가요금이 더해져야한다.")
	void calculateFareWithOverFareLine() {
		//given
		// distance(17): 1450, maxOverFare: 900
		int distanceFare = SubwayFare.calculateDistanceFare(17, 900);
		int expectedFare = 1450 + 900;

		//when/then
		assertThat(distanceFare).isEqualTo(expectedFare);
	}

	@Test
	@DisplayName("로그인 사용자가 청소년인 경우, 할인되어야한다.")
	void calculateFareWithTeenagers() {
		//given
		// distance(17): 1450, maxOverFare: 900
		int discountFare = SubwayFare.calculateReducedFare(2350, 15);
		int expectedFare = (int) ((2350 - 350) * 0.8);

		//when/then
		assertThat(discountFare).isEqualTo(expectedFare);
	}

	@Test
	@DisplayName("로그인 사용자가 어린이인 경우, 할인되어야한다.")
	void calculateFareWithChildren() {
		//given
		// distance(17): 1450, maxOverFare: 900
		int discountFare = SubwayFare.calculateReducedFare(2350, 10);
		int expectedFare = (int) (Math.ceil((2350 - 350) * 0.5));

		//when/then
		assertThat(discountFare).isEqualTo(expectedFare);
	}
}
