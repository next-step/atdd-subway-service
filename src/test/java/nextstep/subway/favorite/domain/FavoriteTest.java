package nextstep.subway.favorite.domain;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import nextstep.subway.common.ErrorCode;
import nextstep.subway.favorite.exception.FavoriteException;
import nextstep.subway.member.domain.Member;
import nextstep.subway.station.domain.Station;

@DisplayName("단위테스트 - 즐겨찾기 도메인")
class FavoriteTest {

	private Station source;
	private Station target;

	@BeforeEach
	void setup() {
		source = Station.from("강남역");
		target = Station.from("광교역");
	}

	@DisplayName("즐겨찾기의 소유자인지 판단하는 메소드 테스트")
	@Test
	void IsOwner() {
		// given
		Member member = mock(Member.class);
		when(member.getId()).thenReturn(1L);
		Favorite favorite = Favorite.of(source, target, member);

		// when
		boolean owner = favorite.isOwner(member);

		// then
		assertThat(owner).isTrue();
	}

	@DisplayName("즐겨찾기의 소유자가 아닐시 예외처리 테스트")
	@Test
	void validIsOwner() {
		// given
		Member member = mock(Member.class);
		Member expectMember = mock(Member.class);
		when(member.getId()).thenReturn(1L);
		when(member.getId()).thenReturn(2L);
		Favorite favorite = Favorite.of(source, target, member);

		// when // then
		assertThatThrownBy(() -> {
			favorite.validOwner(expectMember);
		}).isInstanceOf(FavoriteException.class)
			.hasMessageContaining(ErrorCode.VALID_CAN_NOT_REMOVE_FAVORITE.getErrorMessage());
	}
}