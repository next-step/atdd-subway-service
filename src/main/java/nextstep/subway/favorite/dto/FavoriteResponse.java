package nextstep.subway.favorite.dto;

import java.util.List;
import java.util.stream.Collectors;

import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.station.domain.Station;

public class FavoriteResponse {

	private Long id;
	private Station source;
	private Station target;

	public FavoriteResponse(final Long id, final Station source, final Station target) {
		this.id = id;
		this.source = source;
		this.target = target;
	}

	public static FavoriteResponse of(final Favorite favorite) {
		return new FavoriteResponse(favorite.getId(), favorite.getSource(), favorite.getSource());
	}

	public Long getId() {
		return id;
	}

	public Station getSource() {
		return source;
	}

	public Station getTarget() {
		return target;
	}

	public static List<FavoriteResponse> ofList(List<Favorite> favorites) {
		return favorites.stream()
			.map(FavoriteResponse::of)
			.collect(Collectors.toList());
	}
}
