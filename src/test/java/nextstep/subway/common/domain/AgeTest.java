package nextstep.subway.common.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayName("나이 테스트")
class AgeTest {

	@DisplayName("나이 생성")
	@Test
	void createAge() {
		assertThatNoException()
			.isThrownBy(() -> Age.from(20));
	}

	@DisplayName("나이 생성 - 0보다 작은 수")
	@ParameterizedTest
	@ValueSource(ints = {-1, -2, -3})
	void createAge_null(int value) {
		assertThatIllegalArgumentException()
			.isThrownBy(() -> Age.from(value))
			.withMessageEndingWith("0보다 작을 수 없습니다.");
	}

}