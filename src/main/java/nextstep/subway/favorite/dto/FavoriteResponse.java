package nextstep.subway.favorite.dto;

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
