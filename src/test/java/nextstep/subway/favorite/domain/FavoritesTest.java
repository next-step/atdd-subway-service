package nextstep.subway.favorite.domain;

import static nextstep.subway.generator.StationGenerator.*;
import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;
import java.util.Collections;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import nextstep.subway.Favorite.domain.Favorite;
import nextstep.subway.Favorite.domain.Favorites;
import nextstep.subway.Favorite.dto.FavoritesResponse;
import nextstep.subway.common.domain.Name;
import nextstep.subway.station.dto.StationResponse;

@DisplayName("즐겨찾기 목록 테스트")
class FavoritesTest {

	@DisplayName("즐겨찾기 목록 생성")
	@Test
	void createFavoritesTest() {
		Assertions.assertThatNoException()
			.isThrownBy(() -> Favorites.from(
				Arrays.asList(Favorite.of(station(Name.from("강남역")), station(Name.from("역삼역")), 1L))));
	}

	@DisplayName("즐겨찾기 목록 생성 - 즐겨찾기가 없으면 예외 발생")
	@Test
	void createFavoritesWithoutFavoriteTest() {
		assertThatIllegalArgumentException()
			.isThrownBy(() -> Favorites.from(null))
			.withMessageEndingWith("필수입니다.");
	}

	@DisplayName("즐겨찾기 목록 변환")
	@Test
	void convertFavoritesTest() {
		Favorites favorites = Favorites.from(
			Collections.singletonList(Favorite.of(station(Name.from("강남역")), station(Name.from("역삼역")), 1L)));
		assertThat(favorites.mapToList(
			it -> FavoritesResponse.of(1L, StationResponse.of(it.source()), StationResponse.of(it.target()))))
			.hasSize(1);
	}
}