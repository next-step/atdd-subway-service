package nextstep.subway.favorite.dto;

import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.station.dto.StationResponse;

import java.util.List;
import java.util.stream.Collectors;

public class FavoriteResponse {
	private long favoriteId;
	private StationResponse source;
	private StationResponse target;

	public FavoriteResponse(long favoriteId, StationResponse source, StationResponse target) {
		this.favoriteId = favoriteId;
		this.source = source;
		this.target = target;
	}

	public static FavoriteResponse of(Favorite favorite) {
		return new FavoriteResponse(
				favorite.getId(),
				StationResponse.of(favorite.getSource()),
				StationResponse.of(favorite.getTarget())
		);
	}

	public static List<FavoriteResponse> of(List<Favorite> favorites) {
		return favorites.stream().map(FavoriteResponse::of)
				.collect(Collectors.toList());
	}

	public long getFavoriteId() {
		return favoriteId;
	}

	public StationResponse getSource() {
		return source;
	}

	public StationResponse getTarget() {
		return target;
	}
}
