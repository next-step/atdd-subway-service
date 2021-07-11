package nextstep.subway.favorite.dto;

import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;

public class FavoriteResponse {
	private Long id;
	private StationResponse source;
	private StationResponse target;

	public FavoriteResponse() {
	}

	public FavoriteResponse(Long id, StationResponse source, StationResponse target) {
		this.id = id;
		this.source = source;
		this.target = target;
	}

	public static FavoriteResponse of(Long id, Station source, Station target) {
		return new FavoriteResponse(id, StationResponse.of(source), StationResponse.of(target));
	}

	public static FavoriteResponse of(Favorite favorite) {
		return new FavoriteResponse(favorite.getId(), StationResponse.of(favorite.getSourceStation()), StationResponse.of(favorite.getTargetStation()));
	}

	public Long getId() {
		return id;
	}

	public StationResponse getSourceStationResponse() {
		return source;
	}

	public StationResponse getTargetStationResponse() {
		return target;
	}
}
