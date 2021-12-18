package nextstep.subway.line;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import nextstep.subway.common.exception.InternalServerException;
import nextstep.subway.line.domain.Distance;

public class DistanceTest {

	@Test
	@DisplayName("주어진 값과 비교하여 더 작거나 같은지 확인")
	void isLessOrEqualThan_success() {
		Distance distance = new Distance(5);

		assertThat(distance.isLessOrEqualThan(6)).isTrue();
		assertThat(distance.isLessOrEqualThan(5)).isTrue();
		assertThat(distance.isLessOrEqualThan(4)).isFalse();
	}

	@Test
	@DisplayName("작은 숫자를 이용하여 뺄셈이 가능함")
	void minus_success() {
		Distance distance = new Distance(5);

		assertThat(distance.minus(4)).isEqualTo(new Distance(1));
	}

	@Test
	@DisplayName("같거나 더 큰 숫자를 이용해 뺄셈을 시도하면 예외")
	void minus_largerDistance_exception() {
		Distance distance = new Distance(5);

		assertThatThrownBy(() -> distance.minus(5))
			.isInstanceOf(InternalServerException.class)
			.hasMessage("역과 역 사이의 거리보다 좁은 거리를 입력해주세요");

		assertThatThrownBy(() -> distance.minus(6))
			.isInstanceOf(InternalServerException.class)
			.hasMessage("역과 역 사이의 거리보다 좁은 거리를 입력해주세요");
	}
}
