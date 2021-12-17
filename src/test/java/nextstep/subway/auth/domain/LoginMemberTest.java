package nextstep.subway.auth.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class LoginMemberTest {

	@Test
	@DisplayName("생성 테스트")
	public void create() {
		//when
		LoginMember loginMember = new LoginMember(1L, "email@naver.com", 22);
		//then
		assertThat(loginMember).isNotNull();
	}
}
