package nextstep.subway.line.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("단위 테스트 - 거리 도메인")
class DistanceTest {

	@DisplayName("거리가 11km 이하인 경우 판별하는 메소드 테스트")
	@Test
	void isUnderEleven() {
		// given
		Distance distance = Distance.from(10);

		// when
		boolean isUnderEleven = distance.isUnderEleven();

		// then
		assertThat(isUnderEleven).isTrue();
	}

	@DisplayName("거리가 11 ~ 50km인 경우 판별하는 메소드 테스트")
	@Test
	void isUnderFiftyOne() {
		// given
		Distance distance = Distance.from(50);

		// when
		boolean isUnderFiftyOne = distance.isUnderFiftyOne();

		// then
		assertThat(isUnderFiftyOne).isTrue();
	}

	@DisplayName("거리가 51km 이상인 경우 판별하는 메소드 테스트")
	@Test
	void isOverFiftyOne() {
		// given
		Distance distance = Distance.from(51);

		// when
		boolean isOverFiftyOne = distance.isUnderFiftyOne();

		// then
		assertThat(isOverFiftyOne).isFalse();
	}

	@DisplayName("거리가 11 ~ 50km인 경우 추가 요금을 부과할 구간의 횟수를 계산하는 메소드 테스트")
	@Test
	void calculateExtraPriceSizeMinRate() {
		// given
		Distance distance = Distance.from(44);

		// when
		int size = distance.calculateExtraPriceSize();

		// then
		assertThat(size).isEqualTo(9);
	}

	@DisplayName("거리가 51km 이상인 경우 추가 요금을 부과할 구간의 횟수를 계산하는 메소드 테스트")
	@Test
	void calculateExtraPriceSizeMaxRate() {
		// given
		Distance distance = Distance.from(51);

		// when
		int size = distance.calculateExtraPriceSize();

		// then
		assertThat(size).isEqualTo(7);
	}
}
