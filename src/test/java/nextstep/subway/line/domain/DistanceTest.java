package nextstep.subway.line.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class DistanceTest {

	@Test
	void construct_invalid_distance() {
		assertThatExceptionOfType(IllegalArgumentException.class)
			.isThrownBy(() -> new Distance(Distance.MIN_INCLUSIVE - 1));
	}

	@Test
	void plus() {
		final Distance distance = new Distance(3);
		final Distance plusDistance = distance.plus(2);
		assertThat(plusDistance.getDistance()).isEqualTo(5);
	}

	@Test
	void minus() {
		final Distance distance = new Distance(3);
		final Distance minusDistance = distance.minus(2);
		assertThat(minusDistance.getDistance()).isEqualTo(1);
	}
}
