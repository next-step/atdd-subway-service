package nextstep.subway.fare.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class DistanceFareTest {

	@DisplayName("유효하지 않은 거리의 운임료 계산시 예외발생")
	@Test
	void calculate_invalid_distance() {
		assertThatExceptionOfType(IllegalArgumentException.class)
			.isThrownBy(() -> DistanceFare.calculate(-1));
	}

	@DisplayName("기본운임료(10km 이하)")
	@Test
	void calculate_basic() {
		assertThat(DistanceFare.calculate(1)).isEqualTo(Fare.of(0));
		assertThat(DistanceFare.calculate(10)).isEqualTo(Fare.of(0));
	}

	@DisplayName("추가운임료(10km 초과 ~ 50km 이하)")
	@Test
	void calculate_extra() {
		assertThat(DistanceFare.calculate(15)).isEqualTo(Fare.of(100));
		assertThat(DistanceFare.calculate(18)).isEqualTo(Fare.of(200));
		assertThat(DistanceFare.calculate(50)).isEqualTo(Fare.of(800));
	}

	@DisplayName("추가운임료(50km 초과)")
	@Test
	void calculate_over_extra() {
		assertThat(DistanceFare.calculate(58)).isEqualTo(Fare.of(800 + 100));
		assertThat(DistanceFare.calculate(60)).isEqualTo(Fare.of(800 + 200));
		assertThat(DistanceFare.calculate(90)).isEqualTo(Fare.of(800 + 500));
	}
}
