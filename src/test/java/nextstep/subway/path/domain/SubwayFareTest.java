package nextstep.subway.path.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import nextstep.subway.line.domain.Distance;

class SubwayFareTest {
	
	@Test
	@DisplayName("10km 이하 기본요금 테스트")
	public void createBaseFareTest() {
		//when
		SubwayFare subwayFare = SubwayFare.calculate(Distance.of(10));
		//then
		assertThat(subwayFare.value()).isEqualTo(1250);
	}

	@Test
	@DisplayName("10km 초과 ~ 50km 이하 요금 테스트")
	public void crateFareTenToFiftyTest() {
		//when
		SubwayFare subwayFare = SubwayFare.calculate(Distance.of(11));
		//then
		assertThat(subwayFare.value()).isEqualTo(1350);
	}

	@Test
	@DisplayName("50km 초과 요금 테스트")
	public void crateFareMoreThenFiftyTest() {
		//when
		SubwayFare subwayFare = SubwayFare.calculate(Distance.of(59));
		//then
		assertThat(subwayFare.value()).isEqualTo(2250);
	}

}
