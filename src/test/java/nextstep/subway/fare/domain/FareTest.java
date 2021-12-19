package nextstep.subway.fare.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;

class FareTest {

	@Test
	void plus() {
		assertThat(Fare.of(100).plus(Fare.of(100)).getFare()).isEqualTo(200);
	}

	@Test
	void of_invalid_fare() {
		assertThatExceptionOfType(IllegalArgumentException.class)
			.isThrownBy(() -> Fare.of(-1));
	}
}
