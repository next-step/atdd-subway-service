package nextstep.subway.common.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayName("요금 테스트")
class FareTest {

	@DisplayName("요금 생성")
	@ParameterizedTest(name = "{index} => 요금: {0}")
	@ValueSource(ints = {0, 1000, 10000})
	void createFareTest(int value) {
		assertThatNoException()
			.isThrownBy(() -> Fare.from(value));
	}

	@DisplayName("요금 생성 시 0원 미만일 경우 예외")
	@ParameterizedTest(name = "{index} => 요금: {0}")
	@ValueSource(ints = {-1, -10, -1000})
	void createFareWithNegativeTest(int value) {
		assertThatThrownBy(() -> Fare.from(value))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("음수일 수 없습니다.");
	}


}