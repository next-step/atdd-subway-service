package nextstep.subway.line.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class DistanceTest {

	@Test
	@DisplayName("거리 생성")
	public void createDistanceTest() {
		//when
		Distance distance = Distance.of(10);

		//then
		assertThat(distance).isNotNull();
		assertThat(distance).isEqualTo(Distance.of(10));
	}

	@Test
	@DisplayName("거리 더 작은지 비교 테스트(성공)")
	public void distanceLessThanSuccessTest() {
		//given
		//when
		Distance distance = Distance.of(19);
		Distance otherDistance = Distance.of(20);
		//then
		assertThat(distance.lessThan(otherDistance)).isTrue();
	}

	@Test
	@DisplayName("거리 더 작은지 비교 테스트(실패)")
	public void distanceLessThanFailTest() {
		//given
		//when
		Distance distance = Distance.of(20);
		Distance otherDistance = Distance.of(20);
		//then
		assertThat(distance.lessThan(otherDistance)).isFalse();
	}

	@Test
	@DisplayName("거리 감소 테스트")
	public void distanceDecreaseTest() {
		//given
		Distance distance = Distance.of(20);
		Distance otherDistance = Distance.of(15);
		//when
		Distance decreaseDistance = distance.decrease(otherDistance);
		//then
		assertThat(decreaseDistance).isEqualTo(Distance.of(5));
	}

	@Test
	@DisplayName("거리 증가 테스트")
	public void distanceIncreaseTest() {
		//given
		Distance distance = Distance.of(20);
		Distance otherDistance = Distance.of(15);
		//when
		Distance increaseDistance = distance.increase(otherDistance);
		//then
		assertThat(increaseDistance).isEqualTo(Distance.of(35));
	}

}
