package nextstep.subway.favorite.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.member.domain.Member;

class FavoriteTest {

	@Test
	@DisplayName("즐겨찾기 소유주 일치 테스트")
	public void FavoriteIsOwnerTrueTest() {
		//given
		Member member = new Member(1L, "woowahan@naver.com", "password", 22);
		Favorite favorite = new Favorite(member, null, null);
		LoginMember loginMember = new LoginMember(member);
		//when
		//then
		assertThat(favorite.isOwner(loginMember)).isTrue();
	}

	@Test
	@DisplayName("즐겨찾기 소유주 불 일치 테스트")
	public void FavoriteIsOwnerFalseTest() {
		//given
		Member member = new Member(1L, "woowahan@naver.com", "password", 22);
		Favorite favorite = new Favorite(member, null, null);
		LoginMember loginMember = new LoginMember(2L, "other@naver.com", 32);
		//when
		//then
		assertThat(favorite.isOwner(loginMember)).isFalse();
	}
}
