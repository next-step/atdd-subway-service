package nextstep.subway.favorite.domain;

import static nextstep.subway.member.MemberFixture.*;
import static nextstep.subway.station.StationFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import nextstep.subway.member.domain.Member;
import nextstep.subway.station.domain.Station;

@DisplayName("즐겨찾기")
public class FavoriteTest {

	@DisplayName("생성")
	@Test
	void of() {
		// given
		Member member = 윤준석();
		Station source = 교대역();
		Station target = 양재시민의숲역();

		// when
		Favorite favorite = Favorite.of(member, source, target);

		// then
		assertAll(
			() -> assertThat(favorite).isNotNull(),
			() -> assertThat(favorite.getMember()).isEqualTo(member),
			() -> assertThat(favorite.getSource()).isEqualTo(source),
			() -> assertThat(favorite.getTarget()).isEqualTo(target)
		);
	}
}
