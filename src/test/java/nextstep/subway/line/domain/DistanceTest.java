package nextstep.subway.line.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class DistanceTest {

	@Test
	void construct_invalid_distance() {
		assertThatExceptionOfType(IllegalArgumentException.class)
			.isThrownBy(() -> new Distance(Distance.MIN_INCLUSIVE - 1));
	}
}
