package nextstep.subway.common.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayName("email 테스트")
class EmailTest {

	@DisplayName("email 생성")
	@Test
	void createEmail() {
		assertThatNoException()
			.isThrownBy(() -> Email.from("email@email.com"));
	}

	@DisplayName("email 생성 - null")
	@ParameterizedTest
	@NullAndEmptySource
	void createEmail_null(String email) {
		assertThatIllegalArgumentException()
			.isThrownBy(() -> Email.from(email))
			.withMessageEndingWith("필수입니다.");
	}

	@DisplayName("email 생성 - 형식")
	@ParameterizedTest
	@ValueSource(strings = {"abc", "!@#$", "abc!abc.com", "email.com"})
	void createEmail_format() {
		assertThatIllegalArgumentException()
			.isThrownBy(() -> Email.from("email"))
			.withMessageEndingWith("형식이 올바르지 않습니다.");
	}

}