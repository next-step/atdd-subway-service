package nextstep.subway.path.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import nextstep.subway.line.domain.Fare;

class FareTest {

	@DisplayName("요금은 음수가 될 수 없다.")
	@Test
	void nonNegativeTest() {
		assertThatThrownBy(() -> Fare.wonOf(-1))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("요금은 음수가 될 수 없다.");
	}

	@DisplayName("요금에 N 배를 할 수 있다.")
	@Test
	void nTimesTest() {
		Fare 오천원 = Fare.wonOf(5000);

		Fare 오만원 = 오천원.times(10);

		assertThat(오만원).isEqualTo(Fare.wonOf(50000));
	}

	@DisplayName("요금에 요금을 더 할 수 있다.")
	@Test
	void plusTest() {
		Fare 오천원 = Fare.wonOf(5000);
		Fare 오만원 = Fare.wonOf(50000);

		Fare 오만오천원 = 오만원.plus(오천원);

		assertThat(오만오천원).isEqualTo(Fare.wonOf(55000));
	}

	@DisplayName("요금끼리 크기를 비교할 수 있다.")
	@Test
	void comparableTest() {
		Fare 오천원 = Fare.wonOf(5000);
		Fare 오만원 = Fare.wonOf(50000);

		assertThat(오만원.compareTo(오천원)).isEqualTo(1);
	}

}