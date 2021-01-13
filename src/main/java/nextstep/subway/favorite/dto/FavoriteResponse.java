package nextstep.subway.favorite.dto;

import lombok.Getter;
import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;

@Getter
public class FavoriteResponse {
	private final long id;
	private final StationResponse source;
	private final StationResponse target;

	public FavoriteResponse(Long id, Station source, Station target) {
		this.id = id;
		this.source = StationResponse.of(source);
		this.target = StationResponse.of(target);
	}

	public static FavoriteResponse of(Favorite favorite) {
		return new FavoriteResponse(favorite.getId(), favorite.getSource(), favorite.getTarget());
	}
}
