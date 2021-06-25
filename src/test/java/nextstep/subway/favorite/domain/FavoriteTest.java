package nextstep.subway.favorite.domain;

import static nextstep.subway.member.MemberTest.*;
import static nextstep.subway.station.domain.StationTest.*;
import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class FavoriteTest {

	@DisplayName("즐겨찾기는 시작역과 출발역을 가지고 있다.")
	@Test
	void createTest() {
		Favorite favorite = Favorite.of(자바지기, 강남역, 광교역);

		assertThat(favorite.getSource()).isEqualTo(강남역);
		assertThat(favorite.getTarget()).isEqualTo(광교역);
	}

	@DisplayName("즐겨찾기를 한 사용자와 즐겨찾기된 시작역과 도착역은 존재해야 한다.")
	@Test
	void createWithNullTest() {
		assertThatThrownBy(() -> Favorite.of(null, 강남역, 광교역))
			.isInstanceOf(FavoriteException.class);

		assertThatThrownBy(() -> Favorite.of(자바지기, null, 광교역))
			.isInstanceOf(FavoriteException.class);

		assertThatThrownBy(() -> Favorite.of(자바지기, 강남역, null))
			.isInstanceOf(FavoriteException.class);
	}

	@DisplayName("즐겨찾기의 시작역과 도착역은 같을 수 없다.")
	@Test
	void createWithEqualSourceAndTargetTest() {
		assertThatThrownBy(() -> Favorite.of(자바지기, 강남역, 강남역))
			.isInstanceOf(FavoriteException.class)
			.hasMessageContaining("같은 시작역과 도착역을 즐겨찾기 할 수 없습니다.");
	}

	@DisplayName("즐겨찾기를 한 사용자를 판별할 수 있다.")
	@Test
	void isCreateByTest() {
		Favorite favorite = Favorite.of(자바지기, 강남역, 광교역);

		assertThat(favorite.isCreateBy(자바지기)).isTrue();
		assertThat(favorite.isCreateBy(브라운)).isFalse();
	}
}
