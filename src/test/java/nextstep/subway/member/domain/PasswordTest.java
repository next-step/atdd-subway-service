package nextstep.subway.member.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;

@DisplayName("비밀번호 테스트")
class PasswordTest {

	@DisplayName("비밀번호 생성")
	@Test
	void createPasswordTest() {
		assertThatNoException()
			.isThrownBy(() -> Password.from("password"));
	}

	@DisplayName("비밀번호 생성 - empty")
	@ParameterizedTest
	@EmptySource
	void createPasswordWithNullAndEmpty(String value) {
		assertThatIllegalArgumentException()
			.isThrownBy(() -> Password.from(value))
			.withMessageEndingWith("비밀번호 값은 필수입니다.");
	}

}