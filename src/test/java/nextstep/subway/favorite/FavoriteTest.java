package nextstep.subway.favorite;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import nextstep.subway.favortie.domain.Favorite;
import nextstep.subway.member.MemberTest;
import nextstep.subway.station.StationTest;

public class FavoriteTest {

	@DisplayName("생성 테스트")
	@Test
	void createTest() {
		// given
		Favorite expected = Favorite.of(1L, MemberTest.존, StationTest.노포역, StationTest.다대포해수욕장역);

		// when
		Favorite favorite = Favorite.of(1L, MemberTest.존, StationTest.노포역, StationTest.다대포해수욕장역);

		// then
		assertThat(favorite).isEqualTo(expected);
	}

	@DisplayName("같은 경로인지 확인한다")
	@Test
	void createTest2() {
		// given
		Favorite favorite1 = Favorite.of(1L, MemberTest.존, StationTest.노포역, StationTest.다대포해수욕장역);

		// when
		boolean isSamePath = favorite1.isSamePath(StationTest.노포역, StationTest.다대포해수욕장역);

		// then
		assertThat(isSamePath).isTrue();
	}

	@DisplayName("즐겨찾기 맴버 체크 테스트")
	@Test
	void memberCheckTest() {
		// given
		Favorite favorite = Favorite.of(1L, MemberTest.존, StationTest.노포역, StationTest.다대포해수욕장역);

		// when
		boolean result = favorite.isSameMember(MemberTest.토니);

		// then
		assertThat(result).isFalse();
	}

}
