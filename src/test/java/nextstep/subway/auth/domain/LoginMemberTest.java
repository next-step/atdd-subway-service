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

	@Test
	@DisplayName("내 즐겨찾기 일치 테스트")
	public void idDeleteTrueTest () {
		//when
		LoginMember loginMember = new LoginMember(1L, "email@naver.com", 22);

		//then
		assertThat(loginMember.isDelete(1L)).isTrue();
	}

	@Test
	@DisplayName("내 즐겨찾기 불 일치 테스트")
	public void idDeleteFalseTest () {
		//when
		LoginMember loginMember = new LoginMember(1L, "email@naver.com", 22);

		//then
		assertThat(loginMember.isDelete(2L)).isFalse();
	}
}
