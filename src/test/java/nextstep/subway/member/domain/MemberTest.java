package nextstep.subway.member.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.exception.AuthorizationException;

class MemberTest {

	@Test
	@DisplayName("Member update 테스트")
	public void updateTest() {
		//given
		Member member = new Member("woowahan@naver.com", "password", 22);
		Member newMember = new Member("newEmail@naver.com", "newPassword", 33);
		//when
		member.update(newMember);
		//then
		assertThat(member).isEqualTo(newMember);
	}

	@Test
	@DisplayName("Member checkPassword 검증 테스트")
	public void memberCheckPasswordTest() {
		//when
		Member member = new Member("woowahan@naver.com", "password", 22);
		//then
		assertThatThrownBy(() -> member.checkPassword("otherPassword"))
			.isInstanceOf(AuthorizationException.class)
			.hasMessage("비밀번호가 일치하지 않습니다.");
	}

	@Test
	@DisplayName("LoginMember, Member isequal_True 테스트")
	public void MemberIsEqualTrueTest() {
		//given
		Member member = new Member(1L,"woowahan@naver.com", "password", 22);
		//when
		LoginMember loginMember = new LoginMember(member);
		//then
		assertThat(member.isEqual(loginMember)).isTrue();
	}

	@Test
	@DisplayName("LoginMember, Member isequal_Falsel 테스트")
	public void MemberIsEqualFalseTest() {
		//given
		Member member = new Member(1L,"woowahan@naver.com", "password", 22);
		//when
		LoginMember loginMember = new LoginMember(2L,"other@naver.com",  33);;
		//then
		assertThat(member.isEqual(loginMember)).isFalse();
	}
}


