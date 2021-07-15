package nextstep.subway.favorite.dto;

import java.util.List;
import java.util.stream.Collectors;

import nextstep.subway.favorite.domain.Favorite;

public class FavoriteResponses {

	private List<FavoriteResponse> favoriteResponses;

	public static FavoriteResponses of(List<Favorite> favorites) {
		List<FavoriteResponse> favoriteResponses = favorites.stream()
			.map(FavoriteResponse::of)
			.collect(Collectors.toList());

		return new FavoriteResponses(favoriteResponses);
	}

	public FavoriteResponses() {

	}

	public FavoriteResponses(List<FavoriteResponse> favoriteResponses) {
		this.favoriteResponses = favoriteResponses;
	}

	public List<FavoriteResponse> getFavoriteResponses() {
		return favoriteResponses;
	}
}
