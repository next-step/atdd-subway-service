package nextstep.subway.auth.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
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

	@Test
	@DisplayName("로그인 사용자가 어린이인지 검증 로직 테스트")
	public void isChildTest() {
		//given
		LoginMember loginMember = new LoginMember(1L, "email@naver.com", 12);
		//when
		boolean result = loginMember.isChild();
		//then
		assertThat(result).isTrue();
	}

	@Test
	@DisplayName("로그인 사용자가 청소년인지 검증 로직 테스트")
	public void isYouthTest() {
		//given
		LoginMember loginMember = new LoginMember(1L, "email@naver.com", 13);
		//when
		boolean result = loginMember.isYouth();
		//then
		assertThat(result).isTrue();
	}

}
