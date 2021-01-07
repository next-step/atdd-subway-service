package nextstep.subway.line.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class DistanceTest {

	private static final int ORIGIN_DISTANCE_VALUE = 12;
	private Distance originDistance;

	@BeforeEach
	void setUp() {
		originDistance = new Distance(ORIGIN_DISTANCE_VALUE);
	}

	@ParameterizedTest
	@ValueSource(ints = {1, 2, 5, 10, 11})
	@DisplayName("거리를 재계산하면 기존 거리에서 추가된 거리를 뺀 값이 되어야한다.")
	void calculateNewDistance(int newDistance) {
		//when
		originDistance.calculateNewDistance(newDistance);

		//then
		assertThat(originDistance.get()).isEqualTo(ORIGIN_DISTANCE_VALUE - newDistance);
	}

	@Test
	@DisplayName("거리를 재계산할 때, 기존 거리보다 추가된 거리가 같으면 RuntimeException을 throw해야한다.")
	void calculateNewDistanceEqualToOrigin() {
		//when/then
		assertThatThrownBy(() -> originDistance.calculateNewDistance(ORIGIN_DISTANCE_VALUE))
			.isInstanceOf(RuntimeException.class);
	}

	@Test
	@DisplayName("거리를 재계산할 때, 기존 거리보다 추가된 거리가 크면 RuntimeException을 throw해야한다.")
	void calculateNewDistanceGreaterThanOrigin() {
		//when/then
		assertThatThrownBy(() -> originDistance.calculateNewDistance(ORIGIN_DISTANCE_VALUE + 3))
			.isInstanceOf(RuntimeException.class);
	}
}
