package nextstep.subway.fare.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class FareTest {

	@DisplayName("유효하지 않은 거리의 운임 계산시 예외발생")
	@Test
	void calculate_invalid_distance() {
		assertThatExceptionOfType(IllegalArgumentException.class)
			.isThrownBy(() -> Fare.calculate(-1));
	}

	@DisplayName("기본운임(10km 이하)")
	@Test
	void calculate_basic() {
		assertThat(Fare.calculate(1)).isEqualTo(1250);
		assertThat(Fare.calculate(10)).isEqualTo(1250);
	}

	@DisplayName("추가운임(10km 초과 ~ 50km 이하)")
	@Test
	void calculate_extra() {
		assertThat(Fare.calculate(10)).isEqualTo(1250);
		assertThat(Fare.calculate(12)).isEqualTo(1250 + 100);
		assertThat(Fare.calculate(50)).isEqualTo(1250 + 800);
	}

	@DisplayName("추가운임(50km 초과)")
	@Test
	void calculate_over_extra() {
		assertThat(Fare.calculate(58)).isEqualTo(1250 + 800 + 100);
		assertThat(Fare.calculate(60)).isEqualTo(1250 + 800 + 200);
		assertThat(Fare.calculate(90)).isEqualTo(1250 + 800 + 500);
	}
}
