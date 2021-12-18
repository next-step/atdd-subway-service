package nextstep.subway.favorite;

import static org.assertj.core.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import nextstep.subway.favortie.domain.Favorite;
import nextstep.subway.favortie.domain.Favorites;
import nextstep.subway.member.MemberTest;
import nextstep.subway.station.StationTest;

public class FavoritesTest {

	@Test
	@DisplayName("같은 경로가 있는지 체크한다")
	void isSamePathTest() {
		// given
		List<Favorite> favoriteList = new ArrayList<>();
		favoriteList.add(Favorite.of(1L, MemberTest.존, StationTest.노포역, StationTest.다대포해수욕장역));
		favoriteList.add(Favorite.of(2L, MemberTest.존, StationTest.노포역, StationTest.범내골역));
		Favorites favorites = Favorites.of(favoriteList);

		// when
		boolean hasSamePath = favorites.hasSamePath(MemberTest.존, StationTest.노포역, StationTest.다대포해수욕장역);

		// then
		assertThat(hasSamePath).isTrue();
	}
}
