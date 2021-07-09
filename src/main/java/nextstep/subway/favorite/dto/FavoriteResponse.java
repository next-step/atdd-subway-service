package nextstep.subway.favorite.dto;

import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.station.dto.StationResponse;

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
