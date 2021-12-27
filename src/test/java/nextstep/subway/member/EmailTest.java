package nextstep.subway.member;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import nextstep.subway.exception.AppException;
import nextstep.subway.member.domain.Email;

public class EmailTest {

	@Test
	@DisplayName("생성 테스트")
	void createTest() {
		String email = "abc@email.com";
		assertThat(Email.of(email)).isEqualTo(Email.of(email));
	}

	@Test
	@DisplayName("양식 테스트")
	void validateTest() {
		String email = "123123";

		assertThatThrownBy(() -> Email.of(email))
			.isInstanceOf(AppException.class);
	}
}
