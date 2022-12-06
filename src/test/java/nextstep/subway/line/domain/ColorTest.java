package nextstep.subway.line.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

@DisplayName("색상 테스트")
class ColorTest {

	@Test
	@DisplayName("색상 생성")
	void createColorTest() {
		assertThatNoException()
			.isThrownBy(() -> Color.from("red"));
	}

	@ParameterizedTest
	@NullAndEmptySource
	@DisplayName("색상 생성 실패")
	void createColorFailTest(String value) {
		assertThatIllegalArgumentException()
			.isThrownBy(() -> Color.from(value));
	}

}