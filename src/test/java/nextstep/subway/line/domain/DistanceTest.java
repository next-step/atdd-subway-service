package nextstep.subway.line.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayName("거리 테스트")
class DistanceTest {

	@ParameterizedTest
	@ValueSource(ints = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10})
	@DisplayName("거리 생성")
	void createDistanceTest(int value) {
		assertThatNoException().isThrownBy(() -> Distance.from(value));
	}

	@ParameterizedTest
	@ValueSource(ints = {-1, -2, -3, -4, -5, -6, -7, -8, -9, -10})
	@DisplayName("음수로 거리 생성 시 예외")
	void createDistanceFailTest(int value) {
		assertThatIllegalArgumentException()
			.isThrownBy(() -> Distance.from(value));
	}

	@Test
	@DisplayName("거리 더하기")
	void addDistanceTest() {
		Distance distance1 = Distance.from(1);
		Distance distance2 = Distance.from(2);
		Distance distance3 = Distance.from(3);

		assertThat(distance1.add(distance2)).isEqualTo(distance3);
	}

	@Test
	@DisplayName("거리 빼기")
	void subtractDistanceTest() {
		Distance distance1 = Distance.from(1);
		Distance distance2 = Distance.from(2);
		Distance distance3 = Distance.from(3);

		assertThat(distance3.subtract(distance2)).isEqualTo(distance1);
	}

	@ParameterizedTest
	@ValueSource(ints = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10})
	@DisplayName("거리 뺀 값이 음수일 경우 예외")
	void subtractDistanceFailTest(int value) {
		Distance distance1 = Distance.from(0);
		Distance distance2 = Distance.from(value);

		assertThatIllegalArgumentException()
			.isThrownBy(() -> distance1.subtract(distance2));
	}

	@ParameterizedTest
	@CsvSource(value = {"1:false", "2:true", "3:true"}, delimiter = ':')
	@DisplayName("거리 비교")
	void compareDistanceTest(int value, boolean expected) {
		Distance distance1 = Distance.from(2);
		Distance distance2 = Distance.from(value);

		assertThat(distance1.lessOrEqual(distance2)).isEqualTo(expected);
	}
}