package nextstep.subway.common.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

@DisplayName("이름 테스트")
class NameTest {

	@Test
	@DisplayName("이름 생성")
	void createNameTest() {
		assertThatNoException()
			.isThrownBy(() -> Name.from("강남역"));
	}

	@ParameterizedTest
	@NullAndEmptySource
	@DisplayName("이름 생성 실패")
	void createNameFailTest(String value) {
		assertThatIllegalArgumentException()
			.isThrownBy(() -> Name.from(value));
	}



}