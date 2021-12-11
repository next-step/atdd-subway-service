package nextstep.subway.line;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import nextstep.subway.exception.AppException;
import nextstep.subway.line.domain.Distance;

public class DistanceTest {

	@DisplayName("생성 테스트")
	@Test
	void createTest() {
		// given
		int given = 10;

		// when
		Distance distance = Distance.of(given);

		// then
		assertThat(distance).isEqualTo(Distance.of(given));
	}

	@DisplayName("두 거리의 차를 구한다")
	@Test
	void minusTest() {
		// given
		Distance distance1 = Distance.of(10);
		Distance distance2 = Distance.of(9);

		// when
		Distance different = distance1.minus(distance2);

		// then
		assertThat(different).isEqualTo(Distance.of(1));
	}

	@DisplayName("0보다 커야한다")
	@Test
	void validateTest() {
		// given
		int given = 0;

		// when, then
		assertThatThrownBy(() -> Distance.of(given))
			.isInstanceOf(AppException.class);
	}

	@DisplayName("두 거리의 합를 구한다")
	@Test
	void plusTest() {
		// given
		Distance distance1 = Distance.of(1);
		Distance distance2 = Distance.of(2);

		// when
		Distance result = distance1.plus(distance2);

		// then
		assertThat(result).isEqualTo(Distance.of(3));
	}

	@Test
	@DisplayName("두 거리를 비교한다")
	void lessThanAndEqual() {
		// given
		Distance distance1 = Distance.of(1);
		Distance distance2 = Distance.of(2);

		// when, then
		boolean result = distance1.lessThanAndEqual(distance2);

		// then
		assertThat(result).isTrue();
	}

}
