package nextstep.subway.fare.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import nextstep.subway.fare.exception.FareNotFoundException;

class AgeFareTest {

	@DisplayName("운임료를 찾을 수 없는 경우 예외발생")
	@Test
	void calculate_not_found_fare() {
		assertThatExceptionOfType(FareNotFoundException.class)
			.isThrownBy(() -> AgeFare.calculate(10, null));
	}

	@DisplayName("어린이 할인")
	@Test
	void calculate_kid() {
		assertThat(AgeFare.calculate(6, Fare.of(1350))).isEqualTo(Fare.of(500));
		assertThat(AgeFare.calculate(12, Fare.of(1000))).isEqualTo(Fare.of(325));
	}

	@DisplayName("청소년 할인")
	@Test
	void calculate_teenager() {
		assertThat(AgeFare.calculate(13, Fare.of(1000))).isEqualTo(Fare.of(520));
		assertThat(AgeFare.calculate(18, Fare.of(1350))).isEqualTo(Fare.of(800));
	}

	@DisplayName("일반 운임료")
	@Test
	void calculate_general() {
		assertThat(AgeFare.calculate(19, Fare.of(1500))).isEqualTo(Fare.of(1500));
	}
}
