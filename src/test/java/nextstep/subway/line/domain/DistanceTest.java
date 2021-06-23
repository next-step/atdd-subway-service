package nextstep.subway.line.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class DistanceTest {

	@DisplayName("길이 비교 로직 테스트")
	@Test
	void testIsSmallerThan() {
		Distance distance = new Distance(10);
		assertThat(distance.isSmallerThan(new Distance(10))).isTrue();
		assertThat(distance.isSmallerThan(new Distance(11))).isTrue();
	}

	@DisplayName("거리를 빼는 로직 테스트")
	@Test
	void testReduceDistance() {
		Distance distance = new Distance(10);
		distance.reduce(new Distance(5));
		assertThat(distance.getDistance()).isEqualTo(5);
	}
}